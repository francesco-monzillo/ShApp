package Publish_Subscribe;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Main {

    public static void main(String[] args){
       /*ObjectMapper mapper = new ObjectMapper();
        Orders o = new Orders(1);
        try{
            String json = mapper.writeValueAsString(o);
            System.out.println(json);

            Orders or = mapper.readValue(json, o.getClass());
            System.out.println(or.getId());
        }catch (JsonProcessingException e){
            System.out.println("Something's Wrong");
            e.printStackTrace();
        }catch (IOException ioE){
            System.out.println("Problem reading Order ");
        } */ // Prova conversione oggetto in json e viceversa


        CreateHub c = new CreateHub("Bartolini");
    }
}
