import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class DozenAnalyzer extends JFrame implements ActionListener{

	String quote = "";
	JTextField stocksField;
	JLabel quoteLabel;
	JButton calculateBtn;

	JLabel[] lbls;
    CheckboxGroup[]groups;
	Checkbox[] radioPass;
	Checkbox[] radioFail;
	JButton[] gos;
	int score;
	
	public DozenAnalyzer() {
		setSize(1120, 700);
		setTitle("Dozen Stock Analyzezr");
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	    
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
		
		stocksField = new JTextField("", 10);
		stocksField.setFont(new Font("Arial", Font.PLAIN, 18));
		stocksField.requestFocus();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		add(stocksField, c);
		
		quoteLabel = new JLabel("Stock Quote: ");
		quoteLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(quoteLabel, c);
		
		calculateBtn = new JButton();
		calculateBtn.setText("Submit Answers");
		calculateBtn.setFont(new Font("Arial", Font.PLAIN, 18));
		calculateBtn.addActionListener(this);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = 0;
		add(calculateBtn, c);

		String[] questions = {
				"Is the revenue increasing? (Start first by comparing the annual totals. But if the most recent fiscal year is incomplete, compare the most recent quarter with the same quarter in the previous year)",
				"Is the EPS increasing? (Start first by comparing the annual totals. But if the most recent fiscal year is incomplete, compare the most recent quarter with the same quarter in the previous year)",
				"Has the ROE been increasing for two consecutive years? (After Tax ROE)",
				"Is the analyst consensus recommendation a buy or a strong buy?",
				"Have the EPS surprises during the past four quarters all been positive?",
				"Have the consensus EPS forecast numbers increased year over year?",
				"Is the Long Term 5-year number greater than 8%?",
				"Is the value of the PEG Ratio less than 1.0?",
				"Are the comapny's earnings higher than the industry's earnings?",
				"Is the number of days less than 2?",
				"Has the net activity for the past 3 months been positive?",
		};
		lbls = new JLabel[11];
		groups = new CheckboxGroup[11];
		radioPass = new Checkbox[11];
		radioFail = new Checkbox[11];
		gos = new JButton[11];
		
		for (int i = 1; i < 12; i++) {
			lbls[i-1] = new JLabel(questions[i-1]);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 2*i-1;
			c.gridwidth = 3;
	        c.insets = new Insets(10,0,0,0);
			add(lbls[i-1], c);
			
            groups[i-1] = new CheckboxGroup();
            radioPass[i-1] = new Checkbox("Yes", groups[i-1], false);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = 2*i;
			c.gridwidth = 1;
	        c.insets = new Insets(4,10,0,0);
			add(radioPass[i-1], c);
            
            radioFail[i-1] = new Checkbox("No", groups[i-1], false);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 2;
			c.gridy = 2*i;
	        c.insets = new Insets(4,0,0,10);
			add(radioFail[i-1], c);
			
			gos[i-1] = new JButton();
			gos[i-1].setText("Retrieve Data");
			gos[i-1].addActionListener(this);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 2*i;
			add(gos[i-1], c);
		}
	}
	
	public static void main(String[] args) {
		DozenAnalyzer frame = new DozenAnalyzer();
        frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		quote = stocksField.getText();
		score = 0;
		
		if (e.getSource() == calculateBtn) {
			
			for (int i = 0; i < 11; i++) {
				if (radioPass[i].getState()) {
					score++;
				}
			}
			
			String message = "";
			
			if (score == 11)
				message = "Everything is perfect... I would not recommend buying this stock because nothing is absolutely perfect (no sarcasm).";
			else if (score > 7)
				message = "Strongly recommend buying";
			else if (score > 5)
				message = "Recommend buying";
			else if (score > 3)
				message = "Do NOT recommend buying";
			else if (score > 0)
				message = "Strongly recommend NOT buying";
			else
				message = "Do NOT buy";
				
			JOptionPane.showMessageDialog(null,
					"Score: " + score + " / 11\n\n" + message);
			
			
		} else {

			if (doesStockExist(quote)) {
				String[] urls = {
					"http://www.nasdaq.com/symbol/" + quote + "/revenue-eps",
					"http://www.nasdaq.com/symbol/" + quote + "/revenue-eps",
					"http://www.nasdaq.com/symbol/" + quote + "/financials?query=ratios",
					"http://www.nasdaq.com/symbol/" + quote + "/recommendations",
					"http://www.nasdaq.com/symbol/" + quote + "/earnings-surprise",
					"http://www.nasdaq.com/symbol/" + quote + "/earnings-forecast",
					"http://www.nasdaq.com/symbol/" + quote + "/earnings-growth",
					"http://www.nasdaq.com/symbol/" + quote + "/peg-ratio",
					"http://www.nasdaq.com/symbol/" + quote + "/analyst-research",
					"http://www.nasdaq.com/symbol/" + quote + "/short-interest",
					"http://www.nasdaq.com/symbol/" + quote + "/insider-trades" 
				};

				for (int i = 0; i < 11; i++) {
					if (e.getSource() == gos[i]) {
						try {
							java.awt.Desktop.getDesktop().browse(
									java.net.URI.create(urls[i]));
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null,
									"Error: " + e1.getLocalizedMessage());
						}
					}
				}
			} else {
				JOptionPane
						.showMessageDialog(null,
								"Stock Quote does not exist or you do not have internet connection");
			}
		}
	}

	public boolean doesStockExist(String stock) {
		return true;
	}
}
