//package pl.edu.umg.linkumg.controllers;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import pl.edu.umg.linkumg.dto.JobOfferDto;
//import pl.edu.umg.linkumg.models.JobOffer;
//import pl.edu.umg.linkumg.service.JobOfferService;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/joboffer")
//public class JobOfferController {
//    @Autowired
//    private JobOfferService jobOfferService;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @GetMapping("/")
//    public List<JobOfferDto> getAll()
//    {
//        return jobOfferService.findAll().stream().map(this::mapJobOfferToDto).toList();
//    }
//
//    @GetMapping(value = "/{id}")
//    public JobOfferDto getJobOffer(@PathVariable Integer id)
//    {
//        Optional<JobOffer> jobOffer = jobOfferService.findById(id);
//
//        if(jobOffer.isPresent())
//            return mapJobOfferToDto(jobOffer.get());
//
//        return null;
//    }
//
//    @PostMapping(value = "/")
//    public JobOffer saveJobOffer(@RequestBody JobOfferDto jobOfferDto)
//    {
//        JobOffer jobOffer = mapDtoToJobOffer(jobOfferDto);
//
//        jobOffer.setAddedDate(new Date());
//
//        return jobOfferService.saveOrUpdate(jobOffer);
//    }
//
//    @PutMapping(value = "/{id}")
//    public JobOffer updateJobOffer(@PathVariable Integer id, @RequestBody JobOfferDto jobOfferDto)
//    {
//        Optional<JobOffer> jobOffer = jobOfferService.findById(id);
//
//        if(jobOffer.isPresent())
//        {
//            JobOffer getJobOffer = jobOffer.get();
//            getJobOffer.setAddedDate(new Date());
//            getJobOffer.setPathToJobOfferImage(jobOfferDto.getPathToJobOfferImage());
//            getJobOffer.setAddress(jobOfferDto.getAddress());
//            getJobOffer.setCity(jobOfferDto.getCity());
//            getJobOffer.setTitle(jobOfferDto.getTitle());
//            getJobOffer.setSalaryDown(jobOfferDto.getSalaryDown());
//            getJobOffer.setSalaryUp(jobOfferDto.getSalaryUp());
//
//            return jobOfferService.saveOrUpdate(getJobOffer);
//        }
//
//        return null;
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public void deleteJobOffer(@PathVariable Integer id)
//    {
//        jobOfferService.deleteById(id);
//    }
//
//    private JobOfferDto mapJobOfferToDto (JobOffer jobOffer)
//    {
//        return modelMapper.map(jobOffer, JobOfferDto.class);
//    }
//
//    private JobOffer mapDtoToJobOffer(JobOfferDto jobOfferDto)
//    {
//        return modelMapper.map(jobOfferDto, JobOffer.class);
//    }
//}
