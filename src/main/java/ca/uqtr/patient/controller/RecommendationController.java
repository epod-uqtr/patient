package ca.uqtr.patient.controller;

import ca.uqtr.patient.dto.RecommendationDto;
import ca.uqtr.patient.dto.Request;
import ca.uqtr.patient.dto.Response;
import ca.uqtr.patient.service.recommendation.RecommendationService;
import ca.uqtr.patient.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class RecommendationController {

    private ModelMapper modelMapper;
    private RecommendationService recommendationService;

    public RecommendationController(ModelMapper modelMapper, RecommendationService recommendationService) {
        this.modelMapper = modelMapper;
        this.recommendationService = recommendationService;
    }

    @PostMapping(value = "/recommendation")
    @ResponseBody
    public Response addRecommendation(@RequestBody Request request, HttpServletRequest httpRequest){
        String token = httpRequest.getHeader("Authorization").replace("bearer ","");
        RecommendationDto recommendationDto = modelMapper.map(request.getObject(), RecommendationDto.class);
        System.out.println("----------  "+recommendationDto.toString());
        recommendationDto.getProfessional().setId(JwtTokenUtil.getId(token));
        return recommendationService.addRecommendation(recommendationDto);
    }

    @GetMapping(value = "/recommendation")
    @ResponseBody
    public Response getLastRecommendationByPatient(@RequestParam String patientId)  {
        return recommendationService.getLastRecommendationByPatient(patientId);
    }


    @PutMapping(value = "/recommendation")
    @ResponseBody
    public Response responseToRecommendationByPatient(@RequestParam String patientId)  {
        return recommendationService.updateRecommendationByPatient(patientId);
    }
}
