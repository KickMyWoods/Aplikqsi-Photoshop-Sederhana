import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FileMenu {
    private JMenu menu;
    private JFrame frame;

    public FileMenu(JFrame frame) {
        this.frame = frame;
        menu = new JMenu("File");
        String[] fileOptions = {"Import", "Save", "Exit"};

        for (String fileOption : fileOptions) {
            JMenuItem menuItem = new JMenuItem(fileOption);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (fileOption.equals("Import")) {
                        openImage();
                    } else if (fileOption.equals("Save")) {
                        saveImage();
                    } else if (fileOption.equals("Exit")) {
                        System.exit(0);
                    }
                }
            });

            menu.add(menuItem);
        }
    }

    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                ImageIcon originalImage = new ImageIcon(file.getAbsolutePath());
                Image img = originalImage.getImage();

                int imgWidth = img.getWidth(null);
                int imgHeight = img.getHeight(null);

                int frameWidth = frame.getWidth();
                int frameHeight = frame.getHeight();

                double widthRatio = (double) frameWidth / imgWidth;
                double heightRatio = (double) frameHeight / imgHeight;

                double ratio = Math.min(widthRatio, heightRatio);

                int newWidth = (int) (imgWidth * ratio);
                int newHeight = (int) (imgHeight * ratio);

                Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                JLabel label = new JLabel(scaledIcon);

                frame.getContentPane().removeAll();
                frame.add(label);
                frame.revalidate();
                frame.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error in loading image: " + ex.getMessage());
            }
        }
    }




    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                Component component = frame.getContentPane().getComponent(0);
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    ImageIcon imageIcon = (ImageIcon) label.getIcon();
                    if (imageIcon != null) {
                        ImageIO.write(ImageIO.read(new File(imageIcon.getDescription())), "png", file);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error in saving image: " + e.getMessage());
            }
        }
    }

    public JMenu getMenu() {
        return menu;
    }
}