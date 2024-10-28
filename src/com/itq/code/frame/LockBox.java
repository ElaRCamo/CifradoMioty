package com.itq.code.frame;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import com.itq.code.util.BotonEstilizado;
import com.itq.code.util.CargarArchivoTxt;
import com.itq.code.util.CifradoHill;
import com.itq.code.util.CifradoHillMioty;
import com.itq.code.util.CifradoHillMioty2;
import com.itq.code.util.CifrarMensaje;
import com.itq.code.util.DescargarMensaje;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

public class LockBox  extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
    Color colorPrincipal = new Color(40, 42, 54);
    Color colorError = new Color(255,85,85);
    Color colorBlanco = new Color(248,248,242);
    Color colorTrans = new Color(0,0,0,0);
    Color colorVerde = Color.decode("#50fa7b");
    Color colorRosa = Color.decode("#ff79c6");
    Color colorPurpura = Color.decode("#bd93f9");
    Color colorComentario = Color.decode("#6272a4");
    Font fuentePrincipal = new Font("Consolas", Font.PLAIN, 20);
    Font fuentePrincipalBold = new Font("Consolas", Font.BOLD, 20);
    Font fuenteTitulo = new Font("Consolas", Font.BOLD, 45);
    int x, y;
    private JTextArea txtMensaje;
    private BotonEstilizado btnCifrar = new BotonEstilizado("Cifrar", 15, colorRosa, colorPurpura, 0, 50);
    private BotonEstilizado btnDescifrar = new BotonEstilizado("Descifrar", 15, colorVerde, colorRosa, 0, 50);
    private BotonEstilizado btnCargarArchivo = new BotonEstilizado("", 15, colorPrincipal, colorPrincipal, 0, 50);
    private BotonEstilizado btnDescargar = new BotonEstilizado("", 15, colorPrincipal, colorPrincipal, 0, 50);
    private JLabel lblNip ;
    private String mensajeCifrado;
    private String mensajeDescifrado;
    private JButton btnReset;
    char tipoProceso;
	private int[][] clave; 
	private String contenido = "";
    

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LockBox frame = new LockBox();
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	
	public LockBox() {
		getContentPane().setFont(new Font("Consolas", Font.PLAIN, 15));
		getContentPane().setLayout(null);
		JPanel panelPrincipal = new JPanel();
		btnCifrar.setVisible(false);
        btnDescifrar.setVisible(false);
		
		panelPrincipal.setBounds(0, 688, 500, -687);
		panelPrincipal.setOpaque(false);
		GroupLayout gl_panelPrincipal = new GroupLayout(panelPrincipal);
		panelPrincipal.setBackground(new Color(0, 0, 0, 0));
		panelPrincipal.setLayout(gl_panelPrincipal);
		getContentPane().add(panelPrincipal);
		getContentPane().setForeground(colorVerde);
		getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getX();
				y = e.getY();
			}
		});
		getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(getLocation().x + e.getX() - x, getLocation().y + e.getY() - y);
			}
		});

		getContentPane().setBackground(colorPrincipal);
		
		 UIManager.put("OptionPane.background", colorPrincipal);
	     UIManager.put("Panel.background", colorPrincipal);
	     UIManager.put("OptionPane.messageFont", fuentePrincipal);
	     UIManager.put("OptionPane.messageForeground", colorVerde);
		
		/**********************************************************************************************************************/
		/************************************Configuración de la  ventana principal********************************************/
		/**********************************************************************************************************************/

		setSize(500, 695); // Establece el tamaño de la ventana
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // No realiza ninguna acción al cerrar la ventana
		setUndecorated(true); // Elimina la decoración de la ventana (barras de título, bordes, etc.)
		getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Establece el estilo de decoración de la ventana como
																// ninguno
		this.setLocationRelativeTo(null); // Centra la ventana en la pantalla

		// Configura el fondo y el borde de la ventana
		setBackground(colorPrincipal); // Establece el color de fondo de la ventana
		getRootPane().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, colorComentario)); 
		// Configura la ventana para que no sea redimensionable
		setResizable(false);

		// Calcula y establece la ubicación de la ventana en la pantalla
		GraphicsConfiguration config = getGraphicsConfiguration();
		Rectangle bounds = config.getBounds();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);
		int x = bounds.x + bounds.width - insets.right - getWidth() - 10; // Calcula la posición X
		int y = bounds.y + insets.top + 10; // Calcula la posición Y
		setLocation(x, y); // Establece la ubicación de la ventana

		setVisible(true); // Hace visible la ventana
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Establece que al cerrar la ventana, se liberen los recursos asociados

		/**********************************************************************************************************************/
		BotonEstilizado btnSalir = new BotonEstilizado("X", 20, colorPrincipal, colorRosa, 2, 40);
		btnSalir.setBounds(437, 13, 51, 29);
		btnSalir.setFont(fuentePrincipalBold);
		btnSalir.setBorder(BorderFactory.createEmptyBorder());
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		btnSalir.setFocusable(false);
		DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
		listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		getContentPane().add(btnSalir);
		
		JLabel lblEncabezado = new JLabel("L@ckb0x");
		lblEncabezado.setHorizontalAlignment(SwingConstants.CENTER);
		lblEncabezado.setForeground(colorPurpura);
		lblEncabezado.setFont(fuenteTitulo);
		lblEncabezado.setAlignmentX(0.5f);
		lblEncabezado.setBounds(132, 55, 239, 63);
		getContentPane().add(lblEncabezado);
		
		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelBotones.setBackground(colorPrincipal);
		panelBotones.setBounds(296, 522, 192, 173);

		// Establecer layout como null para posicionamiento libre
		panelBotones.setLayout(null);

		// Añadir el panel al contenedor
		getContentPane().add(panelBotones);
						
		ImageIcon iconoSubir = new ImageIcon(getClass().getResource("/com/itq/code/img/subir.png"));
		ImageIcon iconoDescargar = new ImageIcon(getClass().getResource("/com/itq/code/img/descargar.png"));
						
		// Botón Cargar Archivo
		btnCargarArchivo.setBounds(135, 104, 62, 50);
		panelBotones.add(btnCargarArchivo);
		btnCargarArchivo.setIcon(new ImageIcon(LockBox.class.getResource("/com/itq/code/img/libro.png")));
		btnCargarArchivo.setVisible(true);
		btnCargarArchivo.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        Object[] resultado = CargarArchivoTxt.cargarArchivo(LockBox.this);
		        if (resultado != null) {
		            clave = (int[][]) resultado[0];
		            contenido = (String) resultado[1];

		            System.out.println("Mensaje: " + contenido);
		            System.out.println("Clave: ");
		            CifradoHillMioty2.imprimirMatriz(clave);
		            System.out.println("\n \n \n ");

		            // Mostrar el contenido en el campo de texto
		            txtMensaje.setText(contenido); 
		            txtMensaje.setVisible(true);
		            btnCifrar.setVisible(true);
		            btnDescifrar.setVisible(true);
		            lblNip.setVisible(true);
		            btnReset.setVisible(true);
		        }
		    }
		});



		
		// Botón Descargar Archivo 
		btnDescargar.setBounds(69, 104, 71, 50);
		panelBotones.add(btnDescargar);
		btnDescargar.setIcon(new ImageIcon(LockBox.class.getResource("/com/itq/code/img/descargarPocion.png")));
		
				btnReset = new JButton("");
				btnReset.setBounds(12, 102, 71, 52);
				panelBotones.add(btnReset);
				btnReset.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						txtMensaje.setText("");
					}
				});
				btnReset.setIcon(new ImageIcon(LockBox.class.getResource("/com/itq/code/img/reset.png")));
				btnReset.setBackground(colorPrincipal);
				btnReset.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, colorRosa));
				btnReset.setVisible(false);
				btnCifrar.setText("");
				btnCifrar.setBounds(110, 41, 81, 50);
				panelBotones.add(btnCifrar);
				btnCifrar.setIcon(new ImageIcon(LockBox.class.getResource("/com/itq/code/img/caldero.png")));
				
				
				btnCifrar.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        // Generar una clave de cifrado (matriz 3x3)
				        clave = CifradoHillMioty.generarClaveHill(3);
				        tipoProceso = 'c';
				        
				     // Imprimir la matriz generada
				        System.out.println("Inicia Cifrado... ");
				        
				        System.out.println("Clave generada: ");
				        CifradoHillMioty2.imprimirMatriz(clave); 
				        
				        contenido = contenido.toUpperCase().replace("\n", " ");
				        System.out.println("Contenido original: " + contenido);

				        // Cifrar el mensaje
				        mensajeCifrado = CifradoHillMioty2.cifrarMensajeHill(contenido, clave);
				        System.out.println("Mensaje cifrado: " + mensajeCifrado);
				        
				        // Mostrar el mensaje cifrado en el campo de texto
				        txtMensaje.setVisible(true);
				        txtMensaje.setText(mensajeCifrado);
				        
				        // Hacer visible el botón para descargar el mensaje cifrado
				        btnDescargar.setVisible(true);
				    }
				});

				btnCifrar.setFont(new Font("Consolas", Font.BOLD, 20));
				btnDescifrar.setBounds(22, 41, 81, 50);
				panelBotones.add(btnDescifrar);
				btnDescifrar.setText("");
				btnDescifrar.setIcon(new ImageIcon(LockBox.class.getResource("/com/itq/code/img/bolaCristal.png")));
				
				
				// Botón Descifrar con posicionamiento manual
				btnDescifrar.setFont(new Font("Consolas", Font.BOLD, 20));
				btnDescifrar.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				    	 // Imprimir la matriz generada
				    	System.out.println("\nInicia descifrado... ");
				        //CifradoHillMioty2.imprimirMatriz(clave); 
				    	tipoProceso = 'd';

				        // Llamar a la función para descifrar el mensaje usando la misma clave
				        mensajeDescifrado = CifradoHillMioty2.descifrarMensajeHill(mensajeCifrado, clave);
				        System.out.println("Mensaje descifrado: "+mensajeDescifrado);
				        // Mostrar el mensaje descifrado en el campo de texto
				        txtMensaje.setText(mensajeDescifrado);
				        txtMensaje.setVisible(true);

				        // Hacer visible el botón para descargar el mensaje descifrado
				        btnDescargar.setVisible(true);
				    }
				});

				
				
						
		btnDescargar.setVisible(false);
		btnDescargar.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	String contenidoDescarga = txtMensaje.getText();
		    	System.out.println("Mensaje para descarga: " + contenidoDescarga);
		        if (contenidoDescarga != null) {
		        	descargarMensaje(contenidoDescarga, tipoProceso, clave);
		        }
		    }
		});
		
		
		ImageIcon girlIcon = new ImageIcon(getClass().getResource("/com/itq/code/img/girl2.png"));
		JLabel lblGirl = new JLabel("");
		lblGirl.setBounds(0, 412, 300, 283);
		getContentPane().add(lblGirl);
		lblGirl.setIcon(girlIcon);
		
		// Configurar el campo de texto con posicionamiento manual
		txtMensaje =  new JTextArea();
		txtMensaje.setBounds(29, 161, 444, 250);
		getContentPane().add(txtMensaje);
		txtMensaje.setFont(new Font("Consolas", Font.PLAIN, 15));
		txtMensaje.setBackground(colorPrincipal);
		txtMensaje.setForeground(colorVerde);
		txtMensaje.setBorder(null);
		txtMensaje.setVisible(false);
		txtMensaje.setColumns(10);
		txtMensaje.setLineWrap(true); // Permite que el texto se ajuste a la línea
		txtMensaje.setWrapStyleWord(true); // Ajusta por palabra
		getContentPane().add(txtMensaje);
				
						lblNip = new JLabel("NIP");
						lblNip.setBounds(102, 55, 51, 47);
						getContentPane().add(lblNip);
						lblNip.setIcon(new ImageIcon(LockBox.class.getResource("/com/itq/code/img/key.png")));
						lblNip.setFont(new Font("Consolas", Font.PLAIN, 15));
						lblNip.setBackground(colorPrincipal);
						lblNip.setForeground(colorVerde);
						lblNip.setBorder(null);
						lblNip.setVisible(false);

	}
	
	private void descargarMensaje(String mensajeCifrado, char tipoProceso, int[][] clave) {
		System.out.println("\nInicia descarga...");
        DescargarMensaje descargar = new DescargarMensaje();
        String nombreArchivo = JOptionPane.showInputDialog(null, "Introduce el nombre del archivo");
        
        
        if (nombreArchivo != null && !nombreArchivo.isEmpty()) {
            descargar.descargar(mensajeCifrado, nombreArchivo, tipoProceso, clave);
        } else {
            JOptionPane.showMessageDialog(null, "Nombre de archivo no válido");
        }
    }


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}