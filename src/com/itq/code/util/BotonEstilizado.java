package com.itq.code.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.*;


public class BotonEstilizado extends JButton{
	
	 private final Color colorFondoOriginal;
	    private final Color colorRollover;
	    private final int radioBordes;
	    private final int grosorBorde;
	    private final Color colorBorde;

	    public BotonEstilizado(String texto, int tamanoLetra, Color colorFondo, Color colorBorde, int grosorBorde, int radioBordes) {
	        super(texto);

	        this.colorFondoOriginal = colorFondo;
	        this.colorRollover = colorFondo.brighter(); // Color más claro para el estado rollover
	        this.colorBorde = colorBorde;
	        this.grosorBorde = grosorBorde;
	        this.radioBordes = radioBordes;

	        // Configurar el estilo del botón
	        setFont(new Font("Segoe UI", Font.PLAIN, tamanoLetra));
	        setBackground(colorFondo);
	        setForeground(Color.WHITE); // Color del texto
	        setFocusPainted(false);
	        setOpaque(false); // Hacer el botón transparente para aplicar el borde personalizado

	        // Aplicar UI personalizada para manejar el estado rollover
	        setUI(new BotonModernoUI());
	        
	        // Configurar el borde redondeado
	        setBorder(new RoundedBorder(colorBorde, grosorBorde, radioBordes));
	    }

	    private class BotonModernoUI extends BasicButtonUI {
	        @Override
	        public void paint(Graphics g, JComponent c) {
	            JButton button = (JButton) c;
	            Graphics2D g2d = (Graphics2D) g;

	            // Habilitar el antialiasing para suavizar los bordes
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

	            // Establecer el color de fondo según el estado del botón
	            if (button.getModel().isRollover()) {
	                g2d.setColor(colorRollover);
	            } else {
	                g2d.setColor(colorFondoOriginal);
	            }

	            // Dibujar el fondo del botón con bordes redondeados
	            g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), radioBordes, radioBordes);
	            super.paint(g, c);
	        }
	    }

	    private class RoundedBorder implements Border {
	        private final Color colorBorde;
	        private final int grosorBorde;
	        private final int radioBordes;

	        public RoundedBorder(Color colorBorde, int grosorBorde, int radioBordes) {
	            this.colorBorde = colorBorde;
	            this.grosorBorde = grosorBorde;
	            this.radioBordes = radioBordes;
	        }

	        @Override
	        public Insets getBorderInsets(Component c) {
	            return new Insets(grosorBorde, grosorBorde, grosorBorde, grosorBorde);
	        }

	        @Override
	        public boolean isBorderOpaque() {
	            return true;
	        }

	        @Override
	        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	            Graphics2D g2d = (Graphics2D) g.create();
	            // Habilitar el antialiasing para el borde
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            g2d.setColor(colorBorde);
	            g2d.setStroke(new BasicStroke(grosorBorde));
	            g2d.drawRoundRect(x, y, width - 1, height - 1, radioBordes, radioBordes);
	            g2d.dispose();
	        }
	    }
}