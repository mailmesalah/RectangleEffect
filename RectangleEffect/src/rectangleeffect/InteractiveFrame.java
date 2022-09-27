package rectangleeffect;


/*******************************************************

 ********************************************************/
//	PACKAGES NEEDED
import interactivex.CameraEvent;
import interactivex.CameraListener;
import interactivex.EffectManager;
import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.JPanel;

//INTERACTIVE FRAME CLASS
public class InteractiveFrame extends EffectManager implements CameraListener {

    private int nextBubble = 4;    
    RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    
    private ArrayList<Rectangle> allRectangles = new ArrayList<Rectangle>();
    
    public InteractiveFrame() {
        this.setSize(700, 600);
        this.setVisible(true);    
        setConfigurePanel(new JPanel());
        
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent me) {
                if(nextBubble>=4){
                allRectangles.add(new Rectangle(me.getX(),me.getY(),(10),(10)));
                	nextBubble=0;
                }else{
                	nextBubble++;
                }
            }
        });
        animate();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        this.setBackground(Color.black);
        
        for(; allRectangles.size()>10;)allRectangles.remove(0);

        for (int i = 0; i < allRectangles.size(); i++) {
            g2d.setPaint(Color.blue);
            g2d.setStroke(new BasicStroke(2));
            hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.setRenderingHints(hints);
            g2d.draw(allRectangles.get(i));
        }

    }

    public void animate() {

        class RunBack extends Thread {
            RunBack() {
                setDaemon(true);
                start();
            }

            public void run() {
                while (true) {


                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                    }
                    repaint();
                }
            }
        }

        new RunBack();

    }

    /****************************************************************************************/
    public void shapeOnCameraDetected(CameraEvent ce) {
        int x = ce.getX();
        int y = ce.getY();
        Point p = ce.getCenter();
        int width = (int) p.getX();
        int height = (int) p.getY();
        width = 2 * (width - x);
        height = 2 * (height - y);
        double frameWidth = this.getBounds().getWidth();
        double frameHeight = this.getBounds().getHeight();
        double ratioWidth = frameWidth / 320.0;//(((320.0/frameWidth)*8));
        double ratioHeight = frameHeight / 240.0;//(((240.0/frameHeight)*8));
        
        allRectangles.add(new Rectangle((int) (x * ratioWidth), (int) (y * ratioHeight), (int) (width * ratioWidth), (int) (height * ratioHeight)));

    }

    public void clearRectangles(CameraEvent ce) {

        allRectangles.clear();
    }

    /****************************************************************************************/
    public static void main(String[] arg) {
        JFrame jf = new JFrame("Interactive Frame");
        InteractiveFrame iFrame = new InteractiveFrame();
        jf.setSize(700, 600);
        jf.setVisible(true);
        jf.setContentPane(iFrame);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
