import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;
import java.sql.SQLException;

public class FirstWindow extends JFrame {

	/*  [Поля класса]  */
	public static String dataBaseName;
	public static String login;
	public static String pass;
	private JPanel mainPane;
	private JTextField dbField;
	private JTextField loginField;
	private JPasswordField passwordField;

	/*  [Запуск программы]  */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FirstWindow frame = new FirstWindow();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception except) { except.printStackTrace(); }
			}
		});
	}
	/*  [Создание формы]  */
	public FirstWindow() {

		/*  [Главное окно]  */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 520, 210);
		setTitle("Авторизация");
		
		mainPane = new JPanel();
		JLabel dbLabel = new JLabel("Название БД");
		dbField = new JTextField();
		
		mainPane.setBounds(new Rectangle(0, 100, 0, 0));
		mainPane.setBackground(Color.PINK);
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		mainPane.setLayout(null);
		
		/*  [Поле для ввода названия БД]  */
		dbLabel.setHorizontalAlignment(SwingConstants.LEFT);
		dbField.setHorizontalAlignment(SwingConstants.LEFT);
		
		dbLabel.setForeground(new Color(255, 255, 255));
		dbLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		dbLabel.setBounds(10, 13, 180, 27);
		mainPane.add(dbLabel);
		
		
		dbField.setSelectedTextColor(Color.WHITE);
		dbField.setForeground(Color.BLACK);
		dbField.setCaretColor(Color.BLACK);
		dbField.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		dbField.setBackground(Color.WHITE);
		
		dbField.setFont(new Font("Arial", Font.PLAIN, 14));
		dbField.setBounds(192, 14, 295, 30);
		mainPane.add(dbField);
		dbField.setColumns(10);
		
		/*  [Поле для ввода логина пользователя БД]  */
		JLabel loginLabel = new JLabel("Имя пользователя");
		loginLabel.setHorizontalAlignment(SwingConstants.LEFT);
		loginLabel.setForeground(Color.WHITE);
		loginLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		loginLabel.setBounds(10, 54, 180, 27);
		mainPane.add(loginLabel);
		
		loginField = new JTextField();
		loginField.setSelectedTextColor(Color.WHITE);
		loginField.setHorizontalAlignment(SwingConstants.LEFT);
		loginField.setForeground(Color.BLACK);
		loginField.setFont(new Font("Arial", Font.PLAIN, 14));
		loginField.setColumns(10);
		loginField.setCaretColor(Color.BLACK);
		loginField.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		loginField.setBackground(Color.WHITE);
		loginField.setBounds(192, 52, 295, 30);
		mainPane.add(loginField);
		
		/*  [Поле для ввода пароля пользователя БД]  */
		JLabel passwordLabel = new JLabel("Пароль");
		passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		passwordLabel.setBounds(10, 92, 180, 27);
		mainPane.add(passwordLabel);
		
		passwordField = new JPasswordField();
		passwordField.setSelectedTextColor(Color.WHITE);
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		passwordField.setForeground(Color.BLACK);
		passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
		passwordField.setColumns(10);
		passwordField.setCaretColor(Color.BLACK);
		passwordField.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		passwordField.setBackground(Color.WHITE);
		passwordField.setBounds(192, 93, 295, 30);
		mainPane.add(passwordField);
		
		/*  [Кнопка для подключения к БД]  */
		JButton loginButton = new JButton("Авторизоваться");
		loginButton.setBackground(SystemColor.control);
		loginButton.setFocusPainted(false);
		loginButton.setHorizontalTextPosition(SwingConstants.CENTER);
		loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
		loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginButton.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setBounds(10, 130, 477, 37);
		loginButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		try {
        			Settings.login(dbField.getText(), loginField.getText(), new String(passwordField.getPassword()));
        			dataBaseName = dbField.getText();
        			login = loginField.getText();
        			pass = new String(passwordField.getPassword());
        			SecondWindow secWind = new SecondWindow();
    				secWind.setVisible(true);
        		}catch(SQLException ex) {
        			String newEx = ex.getLocalizedMessage();
        			if (newEx.indexOf("база") != -1) {
        				int answer = JOptionPane.showConfirmDialog(null, "Хотите создать базу данных с именем " + dbField.getText() + "?");
        				if (answer == 0) {
        					try {
        						Settings.createDB(dbField.getText(), loginField.getText(), new String(passwordField.getPassword()));
        						JOptionPane.showMessageDialog(null, "База данных создана!", "Готово", JOptionPane.OK_CANCEL_OPTION);
        					}
        					catch (SQLException e) { JOptionPane.showMessageDialog(null, e, "Ошибка", JOptionPane.ERROR_MESSAGE);}
        				}
        			}
        			else { JOptionPane.showMessageDialog(null, newEx, "Ошибка", JOptionPane.ERROR_MESSAGE); }
        			return;
        		}
        	}
        });
		mainPane.add(loginButton);
	}
}
