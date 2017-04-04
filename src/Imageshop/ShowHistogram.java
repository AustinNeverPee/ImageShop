/*
 * 显示灰度直方图
 * @author austin
 * 最后修改时间 2014.11.3
 */

package Imageshop;

import java.awt.*; 
import java.awt.event.*;

import javax.swing.*;

public class ShowHistogram extends JFrame {
	//窗口固定大小
	static final int WIDTH = 400;
	static final int HEIGHT = 300;
	
	//各点灰度值
	private int[][] data; 
	//灰度频率
	private int[] histogram = new int[256]; 
	
	public ShowHistogram() { 
		//窗口标题
		super("图像的灰度直方图");
		JPanel jp = new JPanel();; 
		this.add(jp, BorderLayout.SOUTH); 
		//设置窗口固定大小
		this.setSize(WIDTH, HEIGHT);
		//设置窗口位置
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		int x = (width - WIDTH) / 2;
		int y = (height - HEIGHT) / 2;
		this.setLocation(x, y);
		//设置可视化组件可见
		this.setVisible(true);
		//设置退出事件监听
		addWindowListener(new WindowAdapter(){ 
			public void windowClosing(WindowEvent e){ 
				ShowHistogram.this.dispose(); 
			} 
	    });
		// 添加窗口监听事件 
	    addWindowListener(new WindowAdapter(){ 
	    	public void windowClosing(WindowEvent e){ 
	    		ShowHistogram.this.dispose(); 
	    	} 
	    }); 
	} 
	
	/**
	 * 获取图片各点灰度值
	 * 计算相应的每个灰度频率
	 * @param data
	 * @param iw
	 * @param ih
	 */
	public void getData(int[][] data, int iw, int ih){ 
		this.data = data; 
	    for (int i = 0; i < iw; i++){
	    	for (int j = 0; j < ih; j++) {
	    		int grey = data[i][j]; 
		    	histogram[grey]++; 
			}
	    } 
    
	    // 找出最大的数,进行标准化. 
	    int temp = histogram[0]; 
	    for (int i = 0; i < 256; i++){ 
	      if (temp <= histogram[i]){ 
	          temp = histogram[i]; 
	      } 
	    } 
	    for (int i = 0; i < 256; i++){ 
	      histogram[i] = histogram[i] * 200 / temp; 
	    } 
	 } 
	
	/**
	 * 画出灰度直方图
	 */
	public void paint(Graphics g){ 
		// 画出水平和垂直的轴 
	    g.drawLine(75, 250, 331, 250); 
	    g.drawLine(75, 50, 75, 250); 
	    // 画出横轴坐标 
	    g.drawString("0", 73, 263); 
	    g.drawString("50", 120, 263); 
	    g.drawString("100", 168, 263); 
	    g.drawString("150", 218, 263); 
	    g.drawString("200", 268, 263); 
	    g.drawString("250", 318, 263); 
	    // 画出纵轴坐标 
	    g.drawString("0.5", 58, 145); 
	    g.drawString("1", 65, 60); 
	    // 画出图像的直方图 
	    for (int i = 0; i < 256; i++){ 
	    	g.drawLine(75 + i, 250, 75 + i, 250 - histogram[i]); 
	    }   
	} 
}