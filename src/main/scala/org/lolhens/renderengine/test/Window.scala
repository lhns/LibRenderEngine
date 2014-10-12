package org.lolhens.renderengine.test

import javax.media.opengl.awt.GLCanvas
import javax.swing.JFrame

/**
 * Created by LolHens on 12.10.2014.
 */
class Window extends JFrame {
  val canvas = new GLCanvas()
  getContentPane().add(canvas)

  /*val view = new BeispielszeneView();
  canvas.addGLEventListener(view);

  setSize(500,500);
  setTitle("CAV-Projekt: JOGL - Beispielszene");
  setResizable(false);

  getContentPane().add(canvas,BorderLayout.CENTER);

  addWindowListener(new WindowAdapter()
  {
    public void windowClosing(WindowEvent e)
    {
      System.exit(0);
    }
  });*/
}
