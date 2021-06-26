import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Locale;

public class SecondWindow extends JFrame {

	private JPanel mainPane;
	private DefaultTableModel tableModel;
	private JTable table;
	private JTextField searchValue;
	private JScrollPane scrollPane;
	private JTextField newValue;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton deleteDbButton;
	private int tableRowHeight;
	private JLabel label_1;
	private JLabel label_2;

	/*  [Конструктор класса]  */
	public SecondWindow() {
		tableRowHeight = 40;
		/*  [Настройки окна]  */
		setBounds(100, 100, 682, 467);
		setLocationRelativeTo(null);
		
		mainPane = new JPanel();
		mainPane.setBackground(Color.PINK);
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		mainPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 11));
		scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane.setBackground(UIManager.getColor("MenuBar.shadow"));
		scrollPane.setBounds(20, 43, 417, 368);
		mainPane.add(scrollPane);

		
		/*  [Получим название столбцов в БД]  */
		final Object[] columnsHeader = Settings.getHeaders().toArray();
		Object[][] data = Settings.getInfo();
		
		/*  [Задаем поведение таблицы]  */
		tableModel = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			// Запретим редактирование таблицы
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			};
		};
		
		tableModel.setColumnIdentifiers(columnsHeader); // установим название столбцов в таблице из взятых из БД
		
		for (int i = 0; i < data.length; i++) tableModel.addRow(data[i]); // отрисуем таблицу
		
		table = new JTable(tableModel); // создаем объект таблицы с заданным ранее поведением
		table.setFont(new Font("Arial", Font.PLAIN, 14)); // устанавливаем шрифт
		table.setRowHeight(tableRowHeight); // устанавливаем высоту
		scrollPane.setViewportView(table);
		
		/*  [Выпадающий список из столбцов таблицы в БД]  */
		Object[] fields = Arrays.copyOfRange(columnsHeader, 1, columnsHeader.length);
		
		final JComboBox comboBox = new JComboBox(fields);
		comboBox.setToolTipText("Поиск по столбцу");
		comboBox.setName("");
		comboBox.setPreferredSize(new Dimension(29, 22));
		comboBox.setForeground(SystemColor.controlText);
		comboBox.setBackground(SystemColor.control);
		comboBox.setFocusTraversalKeysEnabled(false);
		comboBox.setBorder(new LineBorder(Color.BLACK, 2));
		comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		comboBox.setBounds(445, 180, 202, 30);
		mainPane.add(comboBox);
		
		/*  [Поле: поиск по значению в столбце, выбранном в выпадающем списке]  */
		searchValue = new JTextField();
		searchValue.setColumns(10);
		//searchValue.setLocale(new Locale("ru"));
		searchValue.setSelectionColor(Color.DARK_GRAY);
		searchValue.setHorizontalAlignment(SwingConstants.CENTER);
		searchValue.setText("что найти?");
		searchValue.setFocusTraversalKeysEnabled(false);
		searchValue.setBorder(new LineBorder(Color.BLACK, 2));
		searchValue.setBackground(UIManager.getColor("MenuBar.background"));
		searchValue.setFont(new Font("Arial", Font.PLAIN, 14));
		searchValue.setBounds(445, 212, 202, 30);
		PlainDocument doc = (PlainDocument) searchValue.getDocument();
		doc.setDocumentFilter(new TextFilter());
		mainPane.add(searchValue);
		
		/*  [Кнопка: Поиск]  */
		JButton searchButton = new JButton("Поиск");
		searchButton.setFocusTraversalKeysEnabled(false);
		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchButton.setFocusPainted(false);
		searchButton.setBorder(new LineBorder(Color.BLACK, 2));
		searchButton.setBackground(SystemColor.control);
		searchButton.setFont(new Font("Arial", Font.PLAIN, 14));
		searchButton.setBounds(445, 244, 202, 30);
		searchButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent argum) {
        		Object[][] founded;
				try {
					founded = Settings.search(comboBox.getSelectedItem().toString(), searchValue.getText());
					
					int count = tableModel.getRowCount();
	    			int i=0;
	    			while (count>0) {
	    				tableModel.removeRow(i);
	    				count--;
	    			}
	    			
	    			tableModel.setDataVector(founded, columnsHeader);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "По Вашему запросу ничего не найдено...", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
        	}
        });
		mainPane.add(searchButton);
		
		/*  [Кнопка: Добавить]  */
		JButton addButton = new JButton("Добавить информацию");
		addButton.setBackground(SystemColor.control);
		addButton.setFocusTraversalKeysEnabled(false);
		addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addButton.setFocusPainted(false);
		addButton.setBorder(new LineBorder(Color.BLACK, 2));
		addButton.setFont(new Font("Arial", Font.PLAIN, 14));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ThirdWindow window = new ThirdWindow();
				window.setVisible(true);
				dispose();
			}
		});
		addButton.setBounds(445, 43, 202, 30);
		mainPane.add(addButton);
		
		JLabel label = new JLabel("Редактирование базы данных");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		label.setBounds(445, 11, 202, 30);
		mainPane.add(label);
		
		label_1 = new JLabel("Поиск по данным");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Arial", Font.PLAIN, 14));
		label_1.setBounds(445, 148, 202, 30);
		mainPane.add(label_1);
		
		label_2 = new JLabel("Изменить значение строки:");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("Arial", Font.PLAIN, 14));
		label_2.setBounds(445, 285, 202, 30);
		mainPane.add(label_2);
		
		/*  [Поле: Редактирование]  */
		newValue = new JTextField();
		newValue.setBorder(new LineBorder(Color.BLACK, 2));
		newValue.setBackground(SystemColor.control);
		newValue.setFont(new Font("Arial", Font.PLAIN, 14));
		newValue.setEnabled(false);
		newValue.setBounds(445, 317, 202, 30);
		PlainDocument newDoc = (PlainDocument) newValue.getDocument();
		newDoc.setDocumentFilter(new TextFilter());
		
		JLabel updateLabel = new JLabel("Содержимое базы данных");
		updateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		updateLabel.setFont(new Font("Arial", Font.BOLD, 14));
		updateLabel.setBounds(20, 11, 417, 30);
		mainPane.add(updateLabel);
		mainPane.add(newValue);
		
		/*  [Кнопка: Сохранить]  */
		saveButton = new JButton("Сохранить");
		saveButton.setBorder(new LineBorder(Color.BLACK, 2));
		saveButton.setBackground(SystemColor.control);
		saveButton.setFont(new Font("Arial", Font.PLAIN, 14));
		saveButton.setEnabled(false);
		saveButton.setBounds(445, 349, 202, 30);
		mainPane.add(saveButton);
		
		/*  [Кнопка: Удалить]  */
		deleteButton = new JButton("Удалить информацию");
		deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		deleteButton.setBorder(new LineBorder(Color.BLACK, 2));
		deleteButton.setBackground(SystemColor.control);
		deleteButton.setFont(new Font("Arial", Font.PLAIN, 14));
		deleteButton.setBounds(445, 75, 202, 30);
		
		ListSelectionModel selModel = table.getSelectionModel();
		deleteButton.addActionListener(new ActionListener() {
 		   public void actionPerformed(ActionEvent argum)
 		   {
 			   try
 			   {
 				   int selectedRow = table.getSelectedRow();

 				   if (selectedRow == -1) {
 					  JOptionPane.showMessageDialog(null, "Выберите строку для удаления!", "Ошибка", JOptionPane.ERROR_MESSAGE);
 					  return;
 				   }
 				   
 				   int id = (int) tableModel.getValueAt(selectedRow, 0);
 				   Settings.deleteRow(id);
 				   tableModel.removeRow(selectedRow);
 			   }
 			   catch (SQLException e)
 			   {
 				   JOptionPane.showMessageDialog(null, e, "Ошибка", JOptionPane.ERROR_MESSAGE);
 			   }
 		   	}
		});
		
        selModel.addListSelectionListener(new ListSelectionListener() {               
              public void valueChanged(ListSelectionEvent e) {
                   if(table.getSelectedRowCount()==1) {
                	   newValue.setEnabled(true);
                	   saveButton.setEnabled(true);
                	   
                	   saveButton.addActionListener(new ActionListener() {
                		   public void actionPerformed(ActionEvent argum) {
                			   try {
	                			   int selectedRow = table.getSelectedRow();
	                               int selectedColumn = table.getSelectedColumn();
	                               String field = columnsHeader[selectedColumn].toString();
	                			   int id = (int) tableModel.getValueAt(selectedRow, 0);
	                			   String value = newValue.getText();
	                			   Settings.textUpdate(field, value, id);
	                			   tableModel.setValueAt(value, selectedRow, selectedColumn);
                			   } catch (SQLException e) {
                				   JOptionPane.showMessageDialog(null, e, "Ошибка", JOptionPane.ERROR_MESSAGE);
                			   }
                		   	}
                	   });
                   }                  
              }               
         });
		
		mainPane.add(deleteButton);
		
		/*  [Кнопка: Очистить таблицу]  */
		JButton clearButton = new JButton("Стереть всю таблицу");
		clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clearButton.setBorder(new LineBorder(Color.BLACK, 2));
		clearButton.setBackground(SystemColor.control);
		clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
		clearButton.setBounds(445, 107, 202, 30);
		clearButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent argum) {
        		try {
					Settings.clearTable();
					
	    			int count = tableModel.getRowCount();
	    			int i=0;
	    			while (count>0) {
	    				tableModel.removeRow(i);
	    				count--;
	    			}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, e, "Ошибка", JOptionPane.ERROR_MESSAGE);
				}

        	}
        });
		mainPane.add(clearButton);
		
		/*  [Кнопка: Удалить БД]  */
		deleteDbButton = new JButton("Удалить базу данных");
		deleteDbButton.setFocusTraversalKeysEnabled(false);
		deleteDbButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		deleteDbButton.setFocusPainted(false);
		deleteDbButton.setBorder(new LineBorder(Color.BLACK, 2));
		deleteDbButton.setBackground(SystemColor.control);
		deleteDbButton.setFont(new Font("Arial", Font.PLAIN, 14));
		deleteDbButton.setBounds(445, 381, 202, 30);
		deleteDbButton.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent argum) {
			  int answerID = JOptionPane.showConfirmDialog(null, "Вы действительно хотите удалить базу данных?");
			  if (answerID == 0) { // если был дан положительный ответ
			   try {
				   Settings.deleteDb(FirstWindow.dataBaseName);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, e, "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
	 			   dispose();
	 			   JOptionPane.showMessageDialog(null, "База данных успешно удалена!", "Удаление базы данных", JOptionPane.INFORMATION_MESSAGE);
	 		   }
		   }
 	   });
		mainPane.add(deleteDbButton);
	}
}
