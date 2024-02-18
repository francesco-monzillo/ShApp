package Business.receiverClient;

public class main {


    public static void main(String [] args){
        ReceiveFromTopic rfT = new ReceiveFromTopic(args[0], args[1]);
        rfT.receiveMessage(args[0], args[1]);
    }
}
