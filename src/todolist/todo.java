package todolist;
import java.sql.*;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class todo extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					todo frame = new todo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame and its content.
	 */
	Connection con=null;
	public todo() {
		con = DB.dbconnect();
		
	
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1158, 652);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBackground(new Color(153, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Todo-List");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 27));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(469, 25, 149, 53);
		contentPane.add(lblNewLabel);
		
		JLabel lblImportantTask = new JLabel("Important Task");
		lblImportantTask.setHorizontalAlignment(SwingConstants.CENTER);
		lblImportantTask.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblImportantTask.setBounds(48, 150, 149, 53);
		contentPane.add(lblImportantTask);
		
		JLabel lblOtherTask = new JLabel("Other Task");
		lblOtherTask.setHorizontalAlignment(SwingConstants.CENTER);
		lblOtherTask.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblOtherTask.setBounds(48, 213, 149, 53);
		contentPane.add(lblOtherTask);
		/**
		 * Below code is for Text filed and text area where the field is filled by the user
		 * **/
		textField = new JTextField();
		textField.setBounds(229, 162, 213, 34);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(229, 240, 213, 226);
		contentPane.add(textArea);
		
		/**
		 * Below code is for Add button which is visible in the panel and also for functions used by the button(adding a list item).
		 */
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {						
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String imp = textField.getText();
					String other = textArea.getText();
					PreparedStatement pst = con.prepareStatement("insert into todo(important,other) values(?,?)");
					pst.setString(1,imp);
					pst.setString(2,other);
					
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Task Added");
					textField.setText("");
					textArea.setText("");
					int a;
					PreparedStatement pst1 = con.prepareStatement("select * from todo");
					ResultSet rs = pst1.executeQuery();
					ResultSetMetaData rd = rs.getMetaData();
					a = rd.getColumnCount();
					DefaultTableModel df = (DefaultTableModel) table.getModel();
					df.setRowCount(0);
					while(rs.next()) {
						Vector v2 = new Vector();
						for(int i=1;i<a;i++) {
							v2.add(rs.getString("id"));
							v2.add(rs.getString("important"));
							v2.add(rs.getString("other"));
							
						}
						df.addRow(v2);
					}	
				}
				catch(Exception e1) {
					e1.printStackTrace();	
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnNewButton.setBounds(28, 524, 85, 29);
		contentPane.add(btnNewButton);
		
		/**
		 * Below code is for Update button which is visible in the panel and also for functions used by the button(which updates the present list).
		 */
		
		
		JButton btnEdit = new JButton("Update");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel df = (DefaultTableModel)table.getModel();
				int s= table.getSelectedRow();
				int id = Integer.parseInt(df.getValueAt(s, 0).toString());
				
				try {
					String imp=textField.getText();
					String other=textArea.getText();
					PreparedStatement pst=(PreparedStatement)con.prepareStatement("update todo set important=?, other=? where id=?");
					pst.setString(1, imp);
					pst.setString(2, other);
					pst.setInt(3, id);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "list Updated");
					textField.setText("");
					textArea.setText("");
					int a;
					pst=(PreparedStatement) con.prepareStatement("select * from todo");
					ResultSet rs=pst.executeQuery();
					ResultSetMetaData rd=(ResultSetMetaData) rs.getMetaData();
					a=rd.getColumnCount();
					DefaultTableModel df1= (DefaultTableModel) table.getModel();
					df1.setRowCount(0);
					while(rs.next())
					{
					Vector v2=new Vector();
					for (int i=1;i<=a;i++)
					{
					v2.add(rs.getString("id"));
					v2.add(rs.getString("important"));
					v2.add(rs.getString("other"));
					}
					df.addRow(v2);
					}	
				}
				catch(Exception e2) {
					System.out.println(e2);
				}
				
			}
		});
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnEdit.setBounds(136, 524, 103, 29);
		contentPane.add(btnEdit);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					dispose();
					todo frame = new todo();		
					frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnDone.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnDone.setBounds(262, 524, 85, 29);
		contentPane.add(btnDone);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(510, 149, 588, 404);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel df=(DefaultTableModel) table.getModel();
				int selected=table.getSelectedRow();
				int id=Integer.parseInt(df.getValueAt (selected, 0).toString());
				textField.setText (df.getValueAt (selected, 1).toString()); 
				textArea.setText (df.getValueAt (selected, 2).toString());
				btnNewButton.setEnabled(false);
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Important task", "Other task"
			}
		));
		/**
		 * Below code is for Delete button which is visible in the panel and also for functions used by the button(basically deletes a single selected list item).
		 */
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel df = (DefaultTableModel)table.getModel();
				int s= table.getSelectedRow();
				int id = Integer.parseInt(df.getValueAt(s, 0).toString());
				try {
					PreparedStatement pst=(PreparedStatement)con.prepareStatement("delete from todo where id=?");
					pst.setInt(1, id);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Task Deleted");
					int a;
					pst = con.prepareStatement("select * from todo");
					ResultSet rs = pst.executeQuery();
					ResultSetMetaData rd = rs.getMetaData();
					a = rd.getColumnCount();
					DefaultTableModel df1 = (DefaultTableModel) table.getModel();
					df1.setRowCount(0);
					while(rs.next()) {
						Vector v3= new Vector();
						for(int i=1;i<a;i++) {
							v3.add(rs.getString("id"));
							v3.add(rs.getString("important"));
							v3.add(rs.getString("other"));
							
						}
						df1.addRow(v3);
					}	
				}
				catch(Exception e3){
					System.out.println(e3);
				}			
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnDelete.setBounds(357, 524, 85, 29);
		contentPane.add(btnDelete);
	}
}
