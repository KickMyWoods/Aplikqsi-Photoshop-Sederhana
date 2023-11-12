import javax.swing.*;

public class MenuExample {
    public static void main(String[] args) {
        // Frame setting
        JFrame frame = new JFrame("PhotoShop App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Menu sar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // Import menu bar ke main function
        FileMenu fileMenu = new FileMenu(frame);
        EditMenu editMenu = new EditMenu(frame);
        FilterMenu filterMenu = new FilterMenu(frame);

        // Menambahkan menu ke frame
        menuBar.add(fileMenu.getMenu());
        menuBar.add(editMenu.getMenu());
        menuBar.add(filterMenu.getMenu());

        frame.setVisible(true);
    }
}
