package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.itemRepo;
import com.example.demo.dto.memberRepo;
import com.example.demo.dto.urgencyCount;
import com.example.demo.entities.item;
import com.example.demo.entities.member;
import com.example.demo.intrmObj.weatherObj;
import com.example.demo.mappers.JsonHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@Controller
public class homeController {
    
    String temp = "";
    String windSpeed = "";
    weatherObj weathObj = new weatherObj();
    JsonHandler<weatherObj> jsonWeath = new JsonHandler<>();

    JsonHandler<weatherObj> js = new JsonHandler<>();

    @Autowired
    itemRepo itemRep;

    @Autowired
    memberRepo membRepo;

    static class DataPayload {
        private String longitude;
        private String latitude;

        public String getLongitude() {
            return longitude;
        }

        public String getLatitude() {
            return latitude;
        }

    }

    @Autowired
    memberRepo memRepo;

    @Autowired
    itemRepo itmRepo;

    @RequestMapping("/")
    public String home(Model model) throws IOException{
        model.addAttribute("weath", js.getWeatherObject());
        return "index.html";
    }

    @RequestMapping("/complaint")
    public String complaint(Model model) throws IOException{
        model.addAttribute("weath", js.getWeatherObject());
        return "Complaint/complaint_main.html";
    }

    @RequestMapping("/complaint/form")
    public String complaint_form(Model model) throws IOException{
        model.addAttribute("weath", js.getWeatherObject());
        return "Complaint/complaint_form.html";
    }

    @RequestMapping("/complaint/request")
    public String requestForm(Model model) throws IOException{
        model.addAttribute("weath", js.getWeatherObject());
        return "Complaint/request_form.html";
    }

    @RequestMapping("/maintenance_tracking")
    public String maintenance(Model model) throws IOException{
        List<item> itemList = itemRep.findAll();
        model.addAttribute("itemList", itemList);
        model.addAttribute("weath", js.getWeatherObject());
        
        List<urgencyCount> itemListUrgency = itemRep.findUrgencyCount();
        ObjectMapper objMapper = new ObjectMapper();
        String jsonString = objMapper.writeValueAsString(itemListUrgency);
        model.addAttribute("itemListString", jsonString);
        return "maintenanceTracking/maintenance_page.html";
    }

    @RequestMapping("/powerConsumption/leaderboard")
    public String leaderBoard(Model model) throws IOException{
        model.addAttribute("weath", js.getWeatherObject());
        List<member> memberList = membRepo.findAscending();
        model.addAttribute("memberList", memberList);
        
        return "powerConsumption/leaderBoardPage.html";
    }
    
    @RequestMapping("/newMember")
    public String newMember(Model model) throws IOException{
        model.addAttribute("weath", js.getWeatherObject());
        member member_temp = new member();
        model.addAttribute("memberObj", member_temp);
        return "member/newMember.html";
    }

    @PostMapping("/createMember")
    public String createMember(Model model,member member_temp){
        
        member_temp.setWatts_used((long)(Math.random() * (5000 - 1000)) + 1000);
        memRepo.save(member_temp);
        return "redirect:/newMember";
    }

    @RequestMapping("/add_material")
    public String add_material(Model model) throws IOException{
        model.addAttribute("weath", js.getWeatherObject());
        item item_temp = new item();
        model.addAttribute("itemObj", item_temp);
        return "add_material/add_form.html";
    }

    @PostMapping("/createItem")
    public String createItem(Model model,item item_temp){
        itmRepo.save(item_temp);
        return "redirect:/add_material";
    }

    @PostMapping("/send-data")
    public void dataRecv(@RequestBody DataPayload payload) throws IOException {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + payload.getLatitude() + "&longitude="
                + payload.getLongitude()
                + "&current_weather=true";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
        JsonElement weather = jsonObject.get("current_weather");
        JsonObject jsonObject1 = gson.fromJson(weather, JsonObject.class);
        this.temp = jsonObject1.get("temperature").getAsString();
        this.windSpeed = jsonObject1.get("windspeed").getAsString();
        weathObj.setTemp(temp);
        weathObj.setWindSpeed(windSpeed);
        jsonWeath.write(weathObj, "weathStore.json");
    }


}
