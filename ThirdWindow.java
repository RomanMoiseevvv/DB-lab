import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javax.swing.border.LineBorder;

public class ThirdWindow extends JFrame {

	private JPanel mainPane;
	private JTextField idField;
	private JTextField nameField;
	private JTextField placeField;
	private JTextField commField;
	protected static Vector data;

	/*  [Запуск программы]  */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ThirdWindow frame = new ThirdWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*  [Создание формы]  */
	public ThirdWindow() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 360);
		setResizable(false);
		setLocationRelativeTo(null);
		mainPane = new JPanel();
		mainPane.setBackground(Color.PINK);
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		mainPane.setLayout(null);
		
		/*  [Создание полей из БД для последующего заполнения]  */
		Object[] columnsHeader = Settings.getHeaders().toArray();
		
		JLabel id = new JLabel(columnsHeader[0].toString());
		id.setHorizontalAlignment(SwingConstants.CENTER);
		id.setForeground(Color.BLACK);
		id.setFont(new Font("Arial", Font.PLAIN, 14));
		id.setBounds(20, 15, 350, 30);
		mainPane.add(id);
		
		JLabel name = new JLabel(columnsHeader[1].toString());
		name.setHorizontalAlignment(SwingConstants.CENTER);
		name.setForeground(Color.BLACK);
		name.setFont(new Font("Arial", Font.PLAIN, 14));
		name.setBounds(20, 80, 350, 30);
		mainPane.add(name);
		
		JLabel place = new JLabel(columnsHeader[2].toString());
		place.setHorizontalAlignment(SwingConstants.CENTER);
		place.setForeground(Color.BLACK);
		place.setFont(new Font("Arial", Font.PLAIN, 14));
		place.setBounds(20, 140, 350, 30);
		mainPane.add(place);
		
		JLabel comm = new JLabel(columnsHeader[3].toString());
		comm.setHorizontalAlignment(SwingConstants.CENTER);
		comm.setForeground(Color.BLACK);
		comm.setFont(new Font("Arial", Font.PLAIN, 14));
		comm.setBounds(20, 200, 350, 30);
		mainPane.add(comm);
		
		/*  [Создание полей ввода]  */
		idField = new JTextField();
		idField.setBorder(new LineBorder(Color.BLACK, 2));
		idField.setSelectionColor(Color.LIGHT_GRAY);
		idField.setSelectedTextColor(Color.WHITE);
		idField.setForeground(Color.BLACK);
		idField.setCaretColor(Color.BLACK);
		idField.setFocusTraversalKeysEnabled(false);
		idField.setBackground(Color.WHITE);
		idField.setHorizontalAlignment(SwingConstants.CENTER);
		idField.setFont(new Font("Arial", Font.PLAIN, 14));
		idField.setBounds(20, 50, 350, 30);
		mainPane.add(idField);
		idField.setColumns(10);
		
		nameField = new JTextField();
		nameField.setBorder(new LineBorder(Color.BLACK, 2));
		nameField.setSelectionColor(Color.LIGHT_GRAY);
		nameField.setSelectedTextColor(Color.WHITE);
		nameField.setForeground(Color.BLACK);
		nameField.setCaretColor(Color.BLACK);
		nameField.setFocusTraversalKeysEnabled(false);
		nameField.setBackground(Color.WHITE);
		nameField.setHorizontalAlignment(SwingConstants.CENTER);
		nameField.setFont(new Font("Arial", Font.PLAIN, 14));
		nameField.setColumns(10);
		nameField.setBounds(20, 110, 350, 30);
		mainPane.add(nameField);
		
		placeField = new JTextField();
		placeField.setBorder(new LineBorder(Color.BLACK, 2));
		placeField.setSelectionColor(Color.LIGHT_GRAY);
		placeField.setSelectedTextColor(Color.WHITE);
		placeField.setForeground(Color.BLACK);
		placeField.setCaretColor(Color.BLACK);
		placeField.setFocusTraversalKeysEnabled(false);
		placeField.setBackground(Color.WHITE);
		placeField.setHorizontalAlignment(SwingConstants.CENTER);
		placeField.setFont(new Font("Arial", Font.PLAIN, 14));
		placeField.setColumns(10);
		placeField.setBounds(20, 170, 350, 30);
		mainPane.add(placeField);
		
		commField = new JTextField();
		commField.setBorder(new LineBorder(Color.BLACK, 2));
		commField.setSelectionColor(Color.LIGHT_GRAY);
		commField.setSelectedTextColor(Color.WHITE);
		commField.setForeground(Color.BLACK);
		commField.setCaretColor(Color.BLACK);
		commField.setFocusTraversalKeysEnabled(false);
		commField.setBackground(Color.WHITE);
		commField.setHorizontalAlignment(SwingConstants.CENTER);
		commField.setFont(new Font("Arial", Font.PLAIN, 14));
		commField.setColumns(10);
		commField.setBounds(20, 230, 350, 30);
		mainPane.add(commField);
		
		
		/*  [Создание кнопки добавить]  */
		JButton add = new JButton("Добавить");
		add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add.setFocusTraversalKeysEnabled(false);
		add.setHorizontalTextPosition(SwingConstants.CENTER);
		add.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		add.setBackground(SystemColor.control);
		add.setFont(new Font("Arial", Font.PLAIN, 14));
		add.setBounds(19, 275, 352, 30);
		add.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
				try {
					Settings.add(Integer.parseInt(idField.getText()), nameField.getText(), placeField.getText(), Integer.parseInt(commField.getText()));
					SecondWindow window = new SecondWindow();
					window.setVisible(true);
					dispose();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Объект с таким индентификатором уже имеется в базе!", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
        	}
        });
		mainPane.add(add);
	}
}
