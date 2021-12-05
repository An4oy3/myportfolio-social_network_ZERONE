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
