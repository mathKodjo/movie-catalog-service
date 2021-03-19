package com.javacircle.moviecatalogservice.resource;

import com.javacircle.moviecatalogservice.model.CatalogItem;
import com.javacircle.moviecatalogservice.model.Movie;
import com.javacircle.moviecatalogservice.model.Rating;
import com.javacircle.moviecatalogservice.model.UserRating;
import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate  restTemplate;

//    @Autowired
//    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient.Builder webClientBuilder;


    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId")  String userId){

        UserRating ratings = restTemplate.getForObject("http://movie-ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

       return ratings.getUserRating().stream().map(rating ->{

           // method one :restTemplate
           Movie movie = restTemplate.getForObject("http://movie-info-service/moviesInfo/foo?foo=" +rating.getMovieId(), Movie.class);

           // method two :webClient
//           Movie movie = webClientBuilder.build()
//                   .get()
//                   .uri("http://localhost:8081/moviesInfo/foo?foo="+rating.getMovieId())
//                   .retrieve()
//                   .bodyToMono(Movie.class)
//                   .block();

          return  new CatalogItem(movie.getName(), "test", rating.getRating());
       })
       .collect(Collectors.toList());

    }

}
