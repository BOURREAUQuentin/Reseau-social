import java.util.Comparator;

public class MessageDateComparator implements Comparator<Message> {

    @Override
    public int compare(Message msg1, Message msg2) {
        return msg1.getDate().compareTo(msg2.getDate());
    }
}