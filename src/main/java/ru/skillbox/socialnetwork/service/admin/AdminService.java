package ru.skillbox.socialnetwork.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.data.dto.admin.PersonStatisticResponse;
import ru.skillbox.socialnetwork.data.dto.admin.StatisticRequest;
import ru.skillbox.socialnetwork.data.dto.admin.PostStatisticResponse;
import ru.skillbox.socialnetwork.data.entity.Person;
import ru.skillbox.socialnetwork.data.entity.Post;
import ru.skillbox.socialnetwork.data.repository.PersonRepo;
import ru.skillbox.socialnetwork.data.repository.PostRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PostRepository postRepository;
    private final PersonRepo personRepository;

    public PostStatisticResponse getPostStatistic(StatisticRequest request) {
        LocalDateTime from = LocalDateTime.parse(request.getDateFromGraph().substring(0, request.getDateFromGraph().indexOf(" ")));
        LocalDateTime to = LocalDateTime.parse(request.getDateToGraph().substring(0, request.getDateToGraph().indexOf(" ")));
        List<Post> totalPosts = postRepository.findAllByTimeBetweenDates(from, to);
        Map<Timestamp, Long> posts = new TreeMap<>();
        Map<Integer, Double> postsByHour = new HashMap<>();

        List<Post> postsForDiagram = request.getDiagramPeriod().equals("allTime") ? postRepository.findAll() :
                postRepository.findAllByTimeBetweenDates(
                        LocalDateTime.parse(request.getDateFromDiagram().substring(0, request.getDateFromDiagram().indexOf(" "))),
                        LocalDateTime.parse(request.getDateToDiagram().substring(0, request.getDateToDiagram().indexOf(" ")))
                );

        for (int hour = 0; hour < 24; hour++){
            int finalHour = hour;
            long count = postsForDiagram.stream().filter(post -> post.getTime().getHour() == finalHour).count();
            postsByHour.put(hour, (Math.floor(((double) count/postsForDiagram.size() * 100) * 1e2) / 1e2));
        }

        if(request.getGraphPeriod().equals("years")){
            while (from.getYear() <= to.getYear()){
                int finalYear = from.getYear();
                long count = totalPosts.stream().filter(post -> post.getTime().getYear() == finalYear).count();
                posts.put(Timestamp.valueOf(from), count);
                from = from.plusYears(1);
            }
            return postStatisticResponseBuilder(posts, postsByHour, totalPosts.size());
        }
        if (request.getGraphPeriod().equals("months")){
            while (from.isBefore(to)){
                int finalMonth = from.getMonthValue();
                long count = totalPosts.stream().filter(post -> post.getTime().getMonthValue() == finalMonth).count();
                posts.put(Timestamp.valueOf(from), count);
                from = from.plusMonths(1);
            }
            return postStatisticResponseBuilder(posts, postsByHour, totalPosts.size());
        }

            while (from.isBefore(to)){
                int finalDay = from.getDayOfMonth();
                long count = totalPosts.stream().filter(post -> post.getTime().getDayOfMonth() == finalDay).count();
                posts.put(Timestamp.valueOf(from), count);
                from = from.plusDays(1);
            }
            return postStatisticResponseBuilder(posts, postsByHour, totalPosts.size());
    }

    public PersonStatisticResponse getPersonStatistic(StatisticRequest request) {
        LocalDateTime from = LocalDateTime.parse(request.getDateFromGraph().substring(0, request.getDateFromGraph().indexOf(" ")));
        LocalDateTime to = LocalDateTime.parse(request.getDateToGraph().substring(0, request.getDateToGraph().indexOf(" ")));

        List<Person> totalPersons = personRepository.findAllByRegTimeBetweenDates(from, to);
        Map<Timestamp, Long> persons = new TreeMap<>();
        Map<String, Double> ageDistribution = ageDistributionMapBuild(totalPersons);
        Map<String, Integer> sexDistribution = new HashMap<>();

        if(request.getGraphPeriod().equals("years")){
            while (from.getYear() <= to.getYear()){
                int finalYear = from.getYear();
                long count = totalPersons.stream().filter(person -> person.getRegTime().getYear() == finalYear).count();
                persons.put(Timestamp.valueOf(from), count);
                from = from.plusYears(1);
            }
            return personStatisticResponseBuilder(persons, ageDistribution, sexDistribution, totalPersons.size());
        }else if(request.getGraphPeriod().equals("months")){
            while (from.isBefore(to)){
                int finalMonth = from.getMonthValue();
                long count = totalPersons.stream().filter(person -> person.getRegTime().getMonthValue() == finalMonth).count();
                persons.put(Timestamp.valueOf(from), count);
                from = from.plusMonths(1);
            }
        }
        while (from.isBefore(to)){
            int finalDay = from.getDayOfMonth();
            long count = totalPersons.stream().filter(person -> person.getRegTime().getDayOfMonth() == finalDay).count();
            persons.put(Timestamp.valueOf(from), count);
            from = from.plusDays(1L);
        }
        return personStatisticResponseBuilder(persons, ageDistribution, sexDistribution, totalPersons.size());
    }


    private Map<String, Double> ageDistributionMapBuild(List<Person> totalPersons){
        Map<String, Double> ageDistribution = new HashMap<>();
        ageDistribution.put("0-18", 0.0);
        ageDistribution.put("19-23", 0.0);
        ageDistribution.put("24-30", 0.0);
        ageDistribution.put("31-45", 0.0);
        ageDistribution.put("46-60", 0.0);
        ageDistribution.put(">60", 0.0);
        totalPersons.forEach(person -> {
            int age = Period.between(person.getBirthTime().toLocalDate(), LocalDate.now()).getYears();
            if(age > 0 && age <= 18){
                ageDistribution.put("0-18", ageDistribution.get("0-18") + 1);
            }else if(age > 18 && age <= 23){
                ageDistribution.put("19-23", ageDistribution.get("19-23") + 1);
            }else if(age > 23 && age <= 30){
                ageDistribution.put("24-30", ageDistribution.get("24-30") + 1);
            }else if(age > 30 && age <= 45){
                ageDistribution.put("31-45", ageDistribution.get("31-45") + 1);
            }else if(age > 45 && age <= 60){
                ageDistribution.put("46-60", ageDistribution.get("46-60") + 1);
            } else {
                ageDistribution.put(">60", ageDistribution.get(">60") + 1);
            }
        });
        ageDistribution.forEach((s, aDouble) -> ageDistribution.put(s, (Math.floor(((double) aDouble/totalPersons.size() * 100) * 1e2) / 1e2)));
        return ageDistribution;
    }

    private PersonStatisticResponse personStatisticResponseBuilder(Map<Timestamp, Long> persons,
                                                                   Map<String, Double> ageDistribution,
                                                                   Map<String, Integer> sexDistribution,
                                                                   int foundPersonCount){
        return PersonStatisticResponse.builder().error("string")
                .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .totalPersonCount(personRepository.count())
                .foundPersonCount(foundPersonCount)
                .personGraphData(persons)
                .ageDistribution(ageDistribution)
                .sexDistribution(sexDistribution)
                .build();
    }




    private PostStatisticResponse postStatisticResponseBuilder(Map<Timestamp, Long> posts, Map<Integer, Double> postsByHour, int foundPostCount){
        return PostStatisticResponse.builder().error("string")
                .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .totalPostCount(postRepository.count())
                .foundPostCount(foundPostCount)
                .GraphData(posts)
                .postsByHour(postsByHour)
                .build();
    }

}
