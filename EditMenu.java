import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class EditMenu {
    private JMenu menu;
    private JFrame frame;

    public EditMenu(JFrame frame) {
        this.frame = frame;
        menu = new JMenu("Edit");
        String[] editOptions = {"Resize", "Rotate"};

        for (String editOption : editOptions) {
            JMenuItem menuItem = new JMenuItem(editOption);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (editOption.equals("Resize")) {
                        resizeImage();
                    } else if (editOption.equals("Rotate")) {
                        rotateImage();
                    }
                }
            });
            menu.add(menuItem);
        }
    }

    public JMenu getMenu() {
        return menu;
    }

    private void resizeImage() {
        String input = JOptionPane.showInputDialog(frame, "Enter resize percentage:");
        try {
            int percentage = Integer.parseInt(input);
            if (percentage > 0) {
                Component component = frame.getContentPane().getComponent(0);
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    ImageIcon imageIcon = (ImageIcon) label.getIcon();
                    if (imageIcon != null) {
                        Image img = imageIcon.getImage();
                        int newWidth = (int) (img.getWidth(frame) * percentage / 100.0);
                        int newHeight = (int) (img.getHeight(frame) * percentage / 100.0);
                        Image newImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        ImageIcon newIcon = new ImageIcon(newImage);
                        label.setIcon(newIcon);
                        frame.revalidate();
                        frame.repaint();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid percentage (>0).");
            }
        } catch (NumberFormatException | NullPointerException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input.");
        }
    }

    private void rotateImage() {
        Component component = frame.getContentPane().getComponent(0);
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            ImageIcon imageIcon = (ImageIcon) label.getIcon();
            if (imageIcon != null) {
                Image img = imageIcon.getImage();
                BufferedImage bufferedImage = new BufferedImage(img.getWidth(frame), img.getHeight(frame), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(img, 0, 0, frame);
                g2d.dispose();

                // Rotasi gambar 90 derajat
                AffineTransform tx = new AffineTransform();
                tx.rotate(Math.toRadians(90), bufferedImage.getWidth() / 2.0, bufferedImage.getHeight() / 2.0);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                bufferedImage = op.filter(bufferedImage, null);

                Image newImage = bufferedImage.getScaledInstance(bufferedImage.getWidth(), bufferedImage.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon newIcon = new ImageIcon(newImage);
                label.setIcon(newIcon);
                frame.revalidate();
                frame.repaint();
            }
        }
    }

}
