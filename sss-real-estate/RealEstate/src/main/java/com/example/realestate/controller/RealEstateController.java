package com.example.realestate.controller;

import com.example.realestate.enums.*;
import com.example.realestate.model.*;
import com.example.realestate.service.*;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/realEstates")
public class RealEstateController implements ServletContextAware {
    @Autowired
    private RealEstateService realEstateService;

    @Autowired

    private TourService tourService;

    @Autowired
    private LikeOrDisLikeService likeOrDisLikeService;

    @Autowired
    private AgentRateService agentRateService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private UserService userService;

    @Autowired
    ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext){ this.servletContext = servletContext; }

    private String bURL;

    @PostConstruct
    public void init(){ bURL = servletContext.getContextPath(); }


    @GetMapping()
    public ModelAndView index(HttpSession session, HttpServletResponse response) throws IOException {


        List<RealEstate> activeRealEstates = new ArrayList<>();


        activeRealEstates = realEstateService.getAllRealEstates();

        List<Tour> tt= tourService.getAllTours();



        List<TypeRealEstate> tipoviNekretnina = Arrays.asList(TypeRealEstate.values());
        List<TypeSale> tipoviProdaje = Arrays.asList(TypeSale.values());
        List<AgentRate> agentRates = agentRateService.getAllAgentRates();
        List<LikeOrDisLike> likeOrDisLikes = likeOrDisLikeService.getAllLikesOrDisLikes();
        ModelAndView result = new ModelAndView("realEstate");

        try{User us = (User) session.getAttribute("user");
            System.out.println("user: " + us.getId());
            result.addObject("user",us);

            for(RealEstate r : activeRealEstates)
            {
                if(r.getAgent().getId().equals(us.getId())){
                    List<RealEstate> allRealEstates= new ArrayList<RealEstate>();
                    allRealEstates.add(r);
                    result.addObject("realAgent",allRealEstates);
                }
                System.out.println("Real Estate Agency"+ r.getAgency());
            }
        } catch (Exception e) {
            System.out.println("user is null ");

            throw new RuntimeException(e);

        }



        result.addObject("realEstates", activeRealEstates);
        result.addObject("tyRealEstate", tipoviNekretnina);
        result.addObject("tySales", tipoviProdaje);
        result.addObject("tours",tt);
        result.addObject("ld",likeOrDisLikes);
        result.addObject("now", LocalDateTime.now());

        for(LikeOrDisLike li:likeOrDisLikes)
        {
            System.out.println("li.realEstate: " + li.getRealEstate().getId());
            System.out.println("li.user: " + li.getUser().getId());


        }



        for(AgentRate a : agentRates)
        {
            System.out.println("a.description: " + a.getDescription());

        }
        return result;
    }

    @GetMapping(value = "/add")
    public ModelAndView create(HttpSession session, HttpServletResponse response) throws IOException {


        List<TypeRealEstate> tipoviNekretnina = Arrays.asList(TypeRealEstate.values());
        List<TypeSale> tipoviProdaje = Arrays.asList(TypeSale.values());

        ModelAndView result = new ModelAndView("realEstateAdd");
        result.addObject("realEstate", new RealEstate());
        result.addObject("tyRealEstate", tipoviNekretnina);
        result.addObject("tySales", tipoviProdaje);
        return result;
    }

    @PostMapping(value = "/add")
    public String save(HttpSession session ,@ModelAttribute("realEstate") RealEstate realEstate, @RequestParam("file") MultipartFile file, ModelAndView model, HttpServletResponse response) throws IOException {

        try {

            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    Path upath = Paths.get("RealEstate/src/main/resources/static/images/");
                    if (!Files.exists(upath)) {
                        Files.createDirectories(upath);
                    }
                    System.out.println(upath);
                    Path path = upath.resolve(file.getOriginalFilename());
                    Files.write(path, bytes);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }




            realEstate.setPicture(file.getOriginalFilename());
            User user = (User) session.getAttribute("user");


            realEstate.setAgent(user);


            realEstateService.saveRealEstate(realEstate);
            model.addObject("successMessage", "Uspešno ste registrovali nekretninu.");
            System.out.println("Uspesan save");
            System.out.println("Put slike :" + file.getOriginalFilename());
            return "redirect:/realEstates";

        } catch (Exception e) {
            model.addObject("errorMessage", "Greška prilikom dodavanja nekretnine.");
            System.out.println("NEuspesan save");
            return "/realEstateAdd";
        }
    }

    @GetMapping("/detail/{id}")
    public ModelAndView viewDetails(@PathVariable Long id, HttpSession session) {
        Optional<RealEstate> realEstate = realEstateService.getRealEstateById(id);
        ModelAndView modelAndView = new ModelAndView("realEstateDetail");
        User user = (User) session.getAttribute("user");
        modelAndView.addObject("realEstate", realEstate);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editRealEstate(@PathVariable Long id) throws IOException {
        Optional<RealEstate> realEstate = realEstateService.getRealEstateById(id);
        RealEstate realEstate1= realEstate.get();
        if (realEstate.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("realEstateEdit");
            modelAndView.addObject("realEstate", realEstate1);
            modelAndView.addObject("tyRealEstate", Arrays.asList(TypeRealEstate.values()));
            modelAndView.addObject("tySales", Arrays.asList(TypeSale.values()));
            return modelAndView;
        } else {
            System.out.println("Nema nekretnineeeeeeeeeeeeeeeeeeeeeee");
            return new ModelAndView("redirect:/error");

        }
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editRealEstate(@PathVariable Long id,
                                       @ModelAttribute RealEstate realEstate,
                                       HttpServletResponse response
                                       ) throws IOException {

        ModelAndView result = new ModelAndView("realEstateEdit");


            Optional<RealEstate> existingRealEstate = realEstateService.getRealEstateById(id);
            RealEstate existingRealEstateNew=existingRealEstate.get();

            if (existingRealEstate == null) {

                return null;
            }


        existingRealEstateNew.setLocation(realEstate.getLocation());
        existingRealEstateNew.setSurfaceArea(realEstate.getSurfaceArea());
        existingRealEstateNew.setPrice(realEstate.getPrice());
        existingRealEstateNew.setTypeRealEstate(realEstate.getTypeRealEstate());
        existingRealEstateNew.setTypeSales(realEstate.getTypeSales());
        existingRealEstateNew.setPicture(realEstate.getPicture());
        existingRealEstateNew.setActive(realEstate.isActive());

            realEstateService.updateRealEstate(existingRealEstateNew.getId(),existingRealEstateNew);
            result.addObject("success", "Nekretnina uspešno ažurirana.");
            response.sendRedirect(bURL + "/realEstates");


        return result;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteRealEstate(@PathVariable Long id) throws IOException {
        Optional<RealEstate> realEstate = realEstateService.getRealEstateById(id);

        if (realEstate.isEmpty()) {
            System.out.println("Nema nekretnineeeeeeeeeeeeeeeeeeeeeee");
            return new ModelAndView("redirect:/error");
        }


        realEstateService.deleteRealEstate(realEstate.get().getId());


        return new ModelAndView("redirect:/");
    }


//Trebalo bi da se pogleda da li moze da se odradi vise puta search, da ne duplira url

    @GetMapping(value = "/search")
    public ModelAndView search(HttpSession session,
                               @RequestParam(required = false)  String location,
                               @RequestParam(required = false) Integer minArea,
                               @RequestParam(required = false) Integer maxArea,
                               @RequestParam(required = false) Double minPrice,
                               @RequestParam(required = false) Double maxPrice,
                               @RequestParam(required = false) TypeSale typeOfUse,
                               @RequestParam(required = false) TypeRealEstate typeOfRealEstate) {

      /*  ModelAndView result = new ModelAndView("realEstate");


        List<RealEstate> realEstates;


        realEstates = realEstateService.search(realEstateService.getAllRealEstates(),location, minArea, maxArea, minPrice, maxPrice, typeOfUse, typeOfRealEstate);


        List<TypeSale> typeOfUseList = Arrays.asList(TypeSale.values());
        List<TypeRealEstate> typeOfRealEstates = Arrays.asList(TypeRealEstate.values());

        result.addObject("realEstates", realEstates);
        result.addObject("tySales", typeOfUseList);
        result.addObject("tyRealEstate", typeOfRealEstates);

        return result;*/

        ModelAndView result = new ModelAndView("realEstate");
        User user = (User) session.getAttribute("user");

        if (user == null) {
            result.addObject("message", "User not found.");
            return result;
        }


        String prevLocation = (String) session.getAttribute("prevLocation");
        Integer prevMinArea = (Integer) session.getAttribute("prevMinArea");
        Integer prevMaxArea = (Integer) session.getAttribute("prevMaxArea");
        Double prevMinPrice = (Double) session.getAttribute("prevMinPrice");
        Double prevMaxPrice = (Double) session.getAttribute("prevMaxPrice");
        TypeSale prevTypeOfUse = (TypeSale) session.getAttribute("prevTypeOfUse");
        TypeRealEstate prevTypeOfRealEstate = (TypeRealEstate) session.getAttribute("prevTypeOfRealEstate");

        // Check if the current search parameters are the same as the previous ones
        if (Objects.equals(location, prevLocation) &&
                Objects.equals(minArea, prevMinArea) &&
                Objects.equals(maxArea, prevMaxArea) &&
                Objects.equals(minPrice, prevMinPrice) &&
                Objects.equals(maxPrice, prevMaxPrice) &&
                Objects.equals(typeOfUse, prevTypeOfUse) &&
                Objects.equals(typeOfRealEstate, prevTypeOfRealEstate)) {

            result.addObject("message", "Search already performed with the same parameters.");
            return result;
        }

        List<RealEstate> realEstates = realEstateService.search(realEstateService.getAllRealEstates(),
                location, minArea, maxArea, minPrice, maxPrice, typeOfUse, typeOfRealEstate);


        session.setAttribute("prevLocation", location);
        session.setAttribute("prevMinArea", minArea);
        session.setAttribute("prevMaxArea", maxArea);
        session.setAttribute("prevMinPrice", minPrice);
        session.setAttribute("prevMaxPrice", maxPrice);
        session.setAttribute("prevTypeOfUse", typeOfUse);
        session.setAttribute("prevTypeOfRealEstate", typeOfRealEstate);


        List<TypeSale> typeOfUseList = Arrays.asList(TypeSale.values());
        List<TypeRealEstate> typeOfRealEstates = Arrays.asList(TypeRealEstate.values());

        result.addObject("realEstates", realEstates);
        result.addObject("tySales", typeOfUseList);
        result.addObject("tyRealEstate", typeOfRealEstates);
        result.addObject("user",user);

        return result;
    }

    @GetMapping(value="/toure/{id}")
    public ModelAndView reserv(@PathVariable Long id,HttpSession session) throws IOException{
        Optional<RealEstate> realEstate = realEstateService.getRealEstateById(id);
        User user = (User) session.getAttribute("user");
        ModelAndView result = new ModelAndView("tour");
        if(user == null)
        {
            ModelAndView s = new ModelAndView("realEstate");
            s.addObject("error","User is null");
            return s;
        }
        result.addObject("user",user);
        result.addObject("realEstate",realEstate);
        return  result;


    }


    @PostMapping(value="/toure/{id}")
    public void reserv(HttpSession session,@PathVariable Long id,  @RequestParam("datetime") String datetimeStr,HttpServletResponse response) throws IOException
    {

        Optional<RealEstate> realEstate= realEstateService.getRealEstateById(id);
        LocalDateTime tomorrow = LocalDateTime.now();
        User user = (User) session.getAttribute("user");
        ModelAndView re = new ModelAndView("tour");

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(datetimeStr, formatter);
        } catch (DateTimeParseException e) {
            // Handle error for invalid date format
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
            return;
        }


        if(dateTime.isBefore(tomorrow)) {

            re.addObject("error","Cant pick these time, please pick time again");


        }
        if(realEstate.isEmpty()){
            re.addObject("error","Its empty");

        }
        RealEstate res = realEstate.get();
        Tour t = new Tour(dateTime,Status.PENDING,res,user);
        tourService.saveTour(t);
        System.out.println("Uspesno sacuvan tour");
        response.sendRedirect(bURL +"/realEstates");




    }

    @PostMapping(value = "/ac/{id}")
    public void acc(@PathVariable Long id, HttpServletResponse response) throws  IOException
    {
        Optional<Tour> t = tourService.getTourById(id);
        if(t.isEmpty())
        {
            System.out.println("Nema tour-a");
        }
        Tour tour = t.get();
        tour.setStatus(Status.ACCEPT);
        tourService.updateTour(tour.getId(),tour);
        response.sendRedirect(bURL+"/realEstates");
    }

    @PostMapping(value = "/re/{id}")
    public void accRe(@PathVariable Long id, HttpServletResponse response) throws  IOException
    {
        Optional<Tour> t = tourService.getTourById(id);
        if(t.isEmpty())
        {
            System.out.println("Nema tour-a");
        }
        Tour tour = t.get();
        tour.setStatus(Status.REFUSE);
        tourService.updateTour(tour.getId(),tour);
        response.sendRedirect(bURL+"/realEstates");
    }



    @PostMapping(value = "/like/{id}")
    public void like(@PathVariable Long id,HttpServletResponse response,HttpSession session) throws IOException
    {
        Optional<RealEstate> r = realEstateService.getRealEstateById(id);
        List<LikeOrDisLike> likes = likeOrDisLikeService.getAllLikesOrDisLikes();

        if(r.isEmpty())
        {
            System.out.println("Nema real estate-a");
        }
        RealEstate realEstate = r.get();
        User user = (User) session.getAttribute("user");
        if(!user.getRole().equals(Role.USER))
        {
            System.out.println("Pogresan korisnik");
            response.sendRedirect(bURL + "/realEstates");
        }

        for(LikeOrDisLike l : likes)
        {
            if(l.getRealEstate().getId().equals(realEstate.getId())  && l.getUser().getId().equals(user.getId()))
            {
                likeOrDisLikeService.deleteLikeOrDisLike(l.getId());
                System.out.println("Uklonjen dislike");
            }
        }


        LikeOrDisLike like = new LikeOrDisLike(realEstate,user, Feeling.LIKED);
        likeOrDisLikeService.saveLikeOrDisLike(like);
        System.out.println("lajkovao");

        response.sendRedirect(bURL + "/realEstates");

    }

    @PostMapping(value = "/dislike/{id}")
    public void dislike(@PathVariable Long id,HttpServletResponse response,HttpSession session) throws IOException
    {
        Optional<RealEstate> r = realEstateService.getRealEstateById(id);
        List<LikeOrDisLike> likes = likeOrDisLikeService.getAllLikesOrDisLikes();
        if(r.isEmpty())
        {
            System.out.println("Nema real estate-a");
        }
        RealEstate realEstate = r.get();
        User user = (User) session.getAttribute("user");

        if(!user.getRole().equals(Role.USER))
        {
            System.out.println("Pogresan korisnik");
            response.sendRedirect(bURL + "/realEstates");
        }

        for(LikeOrDisLike l : likes)
        {
            if(l.getRealEstate().getId().equals(realEstate.getId())  && l.getUser().getId().equals(user.getId()))
            {
                likeOrDisLikeService.deleteLikeOrDisLike(l.getId());
                System.out.println("Uklonjen like");
            }
        }

        LikeOrDisLike like = new LikeOrDisLike(realEstate,user, Feeling.DISLIKED);
        likeOrDisLikeService.saveLikeOrDisLike(like);
        System.out.println("dislajkovao");

        response.sendRedirect(bURL + "/realEstates");

    }

    @GetMapping(value = "/rate/{id}")
    public ModelAndView rat(@PathVariable Long id,HttpSession session) throws IOException{
        Optional<RealEstate> realEstatee = realEstateService.getRealEstateById(id);
        User user = (User) session.getAttribute("user");
        ModelAndView result = new ModelAndView("rate");
        if(realEstatee.isEmpty())
        {
            result.addObject("error","resd");
        }

        if(user == null)
        {
            ModelAndView s = new ModelAndView("realEstate");
            s.addObject("error","User is null");
            return s;
        }
        RealEstate realEstate = realEstatee.get();
        result.addObject("user",user);
        result.addObject("realEstate",realEstate);
        return  result;


    }

    @PostMapping(value = "/rate/{id}")
    public void rate(@PathVariable Long id, @RequestParam("u") String desc,@RequestParam("rating") int rating, HttpSession session, HttpServletResponse response) throws  IOException
    {
        User user = (User) session.getAttribute("user");
        ModelAndView result = new ModelAndView("rate");
        Optional<RealEstate> realEstatee = realEstateService.getRealEstateById(id);
        if(realEstatee.isEmpty())
        {
            result.addObject("error","resd");
        }
        RealEstate realEstate = realEstatee.get();
        agentRateService.saveAgentRate(new AgentRate(realEstate.getAgent(),user,rating,desc));



        System.out.println("Received rating: " + rating);
        response.sendRedirect(bURL + "/realEstates");
    }



    @GetMapping(value = "/addAg")
    public ModelAndView addAg(HttpSession session, HttpServletResponse response) throws IOException
    {
        User user = (User) session.getAttribute("user");
        List<Agency> agencies = agencyService.getAllAgencies();
        for(Agency a : agencies)
        {
            System.out.println("Email Agency Woner :" + a.getOwner().getEmail());
            if(a.getOwner().equals(user))
            {

                System.out.println("Email :" + user.getEmail());
                ModelAndView r = new ModelAndView("realEstate");
                response.sendRedirect(bURL +"/realEstates");
            }
        }

        ModelAndView result = new ModelAndView("addAgency");
        result.addObject("user",user);
        return result;
    }


    @PostMapping(value = "/addAg")
    public void addAag(HttpSession session ,HttpServletResponse response,@RequestParam("name") String name) throws IOException
    {
        User user = (User) session.getAttribute("user");
        agencyService.saveAgency(new Agency(name,user));
        response.sendRedirect(bURL + "/realEstates");
    }




    @GetMapping(value = "/searchA")
    public ModelAndView searchA(HttpSession session,
                               @RequestParam(required = false)  String location,
                               @RequestParam(required = false) Integer minArea,
                               @RequestParam(required = false) Integer maxArea,
                               @RequestParam(required = false) Double minPrice,
                               @RequestParam(required = false) Double maxPrice,
                               @RequestParam(required = false) TypeSale typeOfUse,
                               @RequestParam(required = false) TypeRealEstate typeOfRealEstate) {

        ModelAndView result = new ModelAndView("realEstate");
        User us = (User) session.getAttribute("user");
        List<RealEstate>realEstates;

        if (us == null) {
            result.addObject("message", "User not found.");
            return result;
        }


        String prevLocation = (String) session.getAttribute("prevLocation");
        Integer prevMinArea = (Integer) session.getAttribute("prevMinArea");
        Integer prevMaxArea = (Integer) session.getAttribute("prevMaxArea");
        Double prevMinPrice = (Double) session.getAttribute("prevMinPrice");
        Double prevMaxPrice = (Double) session.getAttribute("prevMaxPrice");
        TypeSale prevTypeOfUse = (TypeSale) session.getAttribute("prevTypeOfUse");
        TypeRealEstate prevTypeOfRealEstate = (TypeRealEstate) session.getAttribute("prevTypeOfRealEstate");

        // Check if the current search parameters are the same as the previous ones
        if (Objects.equals(location, prevLocation) &&
                Objects.equals(minArea, prevMinArea) &&
                Objects.equals(maxArea, prevMaxArea) &&
                Objects.equals(minPrice, prevMinPrice) &&
                Objects.equals(maxPrice, prevMaxPrice) &&
                Objects.equals(typeOfUse, prevTypeOfUse) &&
                Objects.equals(typeOfRealEstate, prevTypeOfRealEstate)) {

            result.addObject("message", "Search already performed with the same parameters.");
            return result;
        }

        List<RealEstate> allRealEstates= new ArrayList<RealEstate>();

        for(RealEstate r : realEstateService.getAllRealEstates())
        {
            if(r.getAgent().getId().equals(us.getId())){

                allRealEstates.add(r);

                //result.addObject("realAgent",allRealEstates);
            }
        }

        realEstates = realEstateService.search(allRealEstates,location, minArea, maxArea, minPrice, maxPrice, typeOfUse, typeOfRealEstate);
        result.addObject("realEstates", realEstates);




        List<TypeSale> typeOfUseList = Arrays.asList(TypeSale.values());
        List<TypeRealEstate> typeOfRealEstates = Arrays.asList(TypeRealEstate.values());


        result.addObject("tySales", typeOfUseList);
        result.addObject("tyRealEstate", typeOfRealEstates);
        result.addObject("user",us);

        return result;
    }

    @GetMapping(value = "/del")
    public ModelAndView sd(HttpSession session)
    {
        User ue = (User) session.getAttribute("user");
        ModelAndView res = new ModelAndView("deleteAgent");
        res.addObject("user",ue);
        List<User> users = userService.getAllUsers();
        res.addObject("users",users);
        return res;
    }

    @PostMapping(value = "/del/{id}")
    public void d(HttpServletResponse response, @PathVariable Long id) throws IOException
    {




        Optional<User>uu = userService.getUserById(id);
        User u = uu.get();
        u.setActive(false);
        userService.updateUser(id,u);
        response.sendRedirect(bURL + "/realEstates");

    }


    @GetMapping(value = "/sor")
    public ModelAndView sdr(HttpSession session)
    {
        User ue = (User) session.getAttribute("user");
        ModelAndView res = new ModelAndView("popular");
        res.addObject("user",ue);
        List<RealEstate> realEstates = realEstateService.getAllRealEstates();
        List<AgentRate> agentRates = agentRateService.getAllAgentRates();
        List<LikeOrDisLike> likes = likeOrDisLikeService.getAllLikesOrDisLikes();
        List<Tour> tours = tourService.getAllTours();

        realEstates.sort((r1, r2) -> Double.compare(
               realEstateService.calculateRealEstatePopularity(r2, likes, tours),
                realEstateService.calculateRealEstatePopularity(r1, likes, tours))
        );

        List<User> agents = userService.getAllUsers();
        agents.sort((r1,r2)-> Double.compare(userService.getAgentScore(r2,agentRates,tours),
                userService.getAgentScore(r1,agentRates,tours)));

        res.addObject("agents",agents);

        res.addObject("realEstates", realEstates);

        return res;
    }


    @GetMapping(value = "/report")
    public ModelAndView as()
    {
        ModelAndView result = new ModelAndView("report");
        double sumForRental = 0;
        double sumForSale = 0;
        double total = 0;
        List<Tour> tours = tourService.getAllTours();
        for(Tour t :tours) {
            if (t.getStatus() == Status.ACCEPT) {
                Optional<RealEstate> r = realEstateService.getRealEstateById(t.getRealEstate().getId());
                if (r.isEmpty()) {
                    System.out.println("Problem");
                }
                RealEstate realEstate = r.get();
                if (realEstate.getTypeSales() == TypeSale.RENT) {
                    sumForRental += realEstate.getPrice() * 0.01;
                } else {
                    sumForSale += realEstate.getPrice() * 0.001;
                }

            }
        }
        total = sumForRental + sumForSale;
        result.addObject("rent", sumForRental);
        result.addObject("sale", sumForSale);
        result.addObject("total", total);

        return result;

    }


    @GetMapping(value = "/logout")
    public void logout(HttpSession session, HttpServletResponse response) throws IOException
    {
        session.removeAttribute("user");
        response.sendRedirect(bURL + "/login");
    }




}
