package MailService;

public class main {

    public static void main(String[] args){

        try{
            sendMail email = new sendMail("Ciao France","fracmonz@gmail.com","ShAppEmailTest");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
