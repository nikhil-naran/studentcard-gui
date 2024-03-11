import javax.swing.*;
public class GUI {
    public GUI(){
        JFrame frame = new JFrame("GUI");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));



    }



    public static void main(String[] args) {
        new GUI();
    }
}



