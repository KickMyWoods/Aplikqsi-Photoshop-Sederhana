import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;

public class FilterMenu {
        private JMenu menu;
        private JFrame frame;

        public FilterMenu(JFrame frame) {
            this.frame = frame;
            menu = new JMenu("Filter");
            String[] filterOptions = {"B & W", "Grayscale", "Saturation", "Invert", "Sharpening"};

            for (String filterOption : filterOptions) {
                JMenuItem menuItem = new JMenuItem(filterOption);
                menuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        applyFilter(filterOption);
                    }
                });
                menu.add(menuItem);
            }
        }

        private void applyFilter(String filterOption) {
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

                    BufferedImage filteredImage = null;

                    if (filterOption.equals("B & W")) {
                        filteredImage = convertToBlackAndWhite(bufferedImage);
                    } else if (filterOption.equals("Grayscale")) {
                        filteredImage = convertToGrayscale(bufferedImage);
                    } else if (filterOption.equals("Saturation")) {
                        filteredImage = adjustSaturation(bufferedImage);
                    } else if (filterOption.equals("Invert")) {
                        filteredImage = invertColors(bufferedImage);
                    } else if (filterOption.equals("Sharpening")) {
                        filteredImage = sharpenImage(bufferedImage);
                    }

                    if (filteredImage != null) {
                        Image newImage = filteredImage.getScaledInstance(filteredImage.getWidth(), filteredImage.getHeight(), Image.SCALE_SMOOTH);
                        ImageIcon newIcon = new ImageIcon(newImage);
                        label.setIcon(newIcon);
                        frame.revalidate();
                        frame.repaint();
                    }
                }
            }
        }

        private BufferedImage convertToBlackAndWhite(BufferedImage img) {
            ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
            return op.filter(img, null);
        }

        private BufferedImage convertToGrayscale(BufferedImage img) {
            BufferedImage grayImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = grayImage.getGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            return grayImage;
        }

        private BufferedImage adjustSaturation(BufferedImage img) {
            RescaleOp op = new RescaleOp(1.5f, 0, null);
            return op.filter(img, null);
        }

        private BufferedImage invertColors(BufferedImage img) {
            BufferedImage invertedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    int rgba = img.getRGB(x, y);
                    Color col = new Color(rgba, true);
                    col = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());
                    invertedImage.setRGB(x, y, col.getRGB());
                }
            }
            return invertedImage;
        }

        private BufferedImage sharpenImage(BufferedImage img) {
            float[] sharpenArray = { 0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f };
            Kernel kernel = new Kernel(3, 3, sharpenArray);
            ConvolveOp op = new ConvolveOp(kernel);
            return op.filter(img, null);
        }

        public JMenu getMenu() {
            return menu;
        }

}

