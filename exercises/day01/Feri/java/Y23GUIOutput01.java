import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicTextUI.BasicCaret;
import javax.swing.text.JTextComponent;

class Y23GUIOutput01 {

	@SuppressWarnings("serial")
	public static class MyCaret extends BasicCaret {
		@Override public void install(JTextComponent c) { return; }
	}
	
	private List<String> textListe;
	private int currentText;

    boolean buffered;

    JFrame f;
    JTextArea jt;
    JScrollPane sp;
    JLabel lbTextID;
    JSlider slider;
    
    Timer timer;
    
    public Y23GUIOutput01(String title, boolean buffered) {
    	this.textListe = new ArrayList<>();
    	this.currentText = -1;
    	this.buffered = buffered;
		create(title);
	}
    
    public void create(String title) {
        f = new JFrame(title);
        Container p = f.getContentPane();
        
        BoxLayout bl = new BoxLayout(p, BoxLayout.Y_AXIS);
        p.setLayout(bl);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        
        JButton btPrevious = new JButton("<");
        buttonPanel.add(btPrevious);
        btPrevious.addActionListener(ev -> {
        	previous();
        });

        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        
        lbTextID = new JLabel("0");
        buttonPanel.add(lbTextID);
        
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        
        JButton btNext = new JButton(">");
        buttonPanel.add(btNext);
        btNext.addActionListener(ev -> {
        	next();
        });
        
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));

        JButton btSmaller = new JButton("v");
        buttonPanel.add(btSmaller);
        btSmaller.addActionListener(ev -> {
        	smaller();
        });
        
        JButton btBigger = new JButton("^");
        buttonPanel.add(btBigger);
        btBigger.addActionListener(ev -> {
        	bigger();
        });
        
        buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));

        JButton btBuffered = new JButton("Buffered");
        buttonPanel.add(btBuffered);
        btBuffered.addActionListener(ev -> {
        	buffered = ! buffered;
        });
        
        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        
        JButton btAnimation = new JButton("Animation");
        buttonPanel.add(btAnimation);
        btAnimation.addActionListener(ev -> {
        	animation();
        });
        
        p.add(buttonPanel);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 10000, 0);
        slider.addChangeListener(ev -> {
        	double percent = slider.getValue() * 0.0001;
        	int page = (int) (percent * (textListe.size()-1));
        	switchPage(page);
        });
        p.add(slider);
        
        jt = new JTextArea();
        jt.setCaret(new MyCaret());
        jt.setEditable(false);
//        jt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        jt.setFont(new Font("Consolas", Font.PLAIN, 16));
        
        sp = new JScrollPane(jt); 
        p.add(sp);
        
        // set initial size of frame
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    
    private int nextPage = -1;

    private synchronized void switchPage(int page) {
    	if (nextPage == -1) {
        	nextPage = page;
        	SwingUtilities.invokeLater(() -> asyncSwitchPage());
        	return;
    	}
    	nextPage = page;
    }

    
    private synchronized void asyncSwitchPage() {
    	int targetPage = Math.min(textListe.size()-1, Math.max(0, nextPage));
    	if (currentText != targetPage) {
    		currentText = targetPage;
    		String text = textListe.get(currentText);
    		sp.getHorizontalScrollBar().getValue();
    		sp.getVerticalScrollBar().getValue();
        	jt.setText(text);
        	lbTextID.setText(Integer.toString(currentText));   	
    	}
    	nextPage = -1;
	}
    
    
    private void previous() {
		switchPage(currentText-1);
	}
    
    private void next() {
		switchPage(currentText+1);
	}

	public void addStep(String text) {
		if (buffered) {
			textListe.add(text);
			if (currentText != -1) {
				return;
			}
		}
		currentText = textListe.size();
		textListe.add(text);
    	jt.setText(text);
    	lbTextID.setText(Integer.toString(currentText));
    }

    private void smaller() {
    	Font font = jt.getFont();
    	Font smallFont = font.deriveFont(font.getSize2D()/1.1f);
    	jt.setFont(smallFont);
	}

    private void bigger() {
    	Font font = jt.getFont();
    	Font smallFont = font.deriveFont(font.getSize2D()*1.1f);
    	jt.setFont(smallFont);
	}

    private synchronized void animation() {
    	if (timer != null) {
    		timer.stop();
    		timer = null;
    		return;
    	}
    	timer = new Timer(250, ev -> {
    		asyncAnimation();
    	});
    	timer.setInitialDelay(250);
    	timer.start();
	}

	private synchronized void asyncAnimation() {
		if (timer == null) {
			return;
		}
		int page = currentText + 1;
		if (page >= textListe.size()) {
			timer.stop();
			timer = null;
			return;
		}
		switchPage(page);
	}
    
	
}