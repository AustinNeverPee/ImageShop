/*
 * Imageshop主界面
 * @author austin
 * 最后修改时间 2014.12.21
 */

package Imageshop;

import javax.security.auth.Refreshable;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ShopFrame extends JFrame {
	// 窗口固定大小
	static final int WIDTH = 800;
	static final int HEIGHT = 650;
	private Image image;
	// 做“两张图像渐变转换”与“计算两张图像相似度”时存放图片变量
	private Image imageFirst;
	private Image imageSecond;
	// 做“两张图像渐变转换”时两张图片所占比例
	private int proportion;

	public ShopFrame() {
		// 窗口标题
		super("Imageshop");

		// 创建一个菜单条
		JMenuBar menubar1 = new JMenuBar();
		// 将菜单条加入到根面板中
		setJMenuBar(menubar1);
		// 创建几个菜单
		JMenu menu1 = new JMenu("File");
		JMenu menu2 = new JMenu("Process");
		JMenu menu3 = new JMenu("Help");
		JMenu menu4 = new JMenu("Scale");
		JMenu menu5 = new JMenu("Quantize");
		JMenu menu6 = new JMenu("Denoise");
		JMenu menu7 = new JMenu("Histogram Equalization");
		JMenu menu8 = new JMenu("Spatial Filtering");
		JMenu menu9 = new JMenu("Frequency Domain");
		JMenu menu10 = new JMenu("Haze Removal");
		//JMenu menu11 = new JMenu("播放动画");
		// 在菜单条中添加菜单
		menubar1.add(menu1);
		menubar1.add(menu2);
		menubar1.add(menu4);
		menubar1.add(menu5);
		menubar1.add(menu6);
		menubar1.add(menu7);
		menubar1.add(menu8);
		menubar1.add(menu9);
		menubar1.add(menu10);
		//menubar1.add(menu11);
		menubar1.add(menu3);
		// 创建菜单项组件
		JMenuItem item1 = new JMenuItem("Open");
		JMenuItem item2 = new JMenuItem("Save");
		JMenuItem item3 = new JMenuItem("Exit");
		JMenuItem item4 = new JMenuItem("Turn To Gray Image");
		JMenuItem item5 = new JMenuItem("Turn To B&W Image");
		JMenuItem item6 = new JMenuItem("Show Red Component");
		JMenuItem item7 = new JMenuItem("Show Green Component");
		JMenuItem item8 = new JMenuItem("Show Blue Component");
		JMenuItem item9 = new JMenuItem("About");
		JMenuItem item10 = new JMenuItem("最临近插值算法（等距采样法）放缩");
		JMenuItem item11 = new JMenuItem("双线性差值算法放缩");
		JMenuItem item12 = new JMenuItem("双立方插值算法放缩");
		JMenuItem item13 = new JMenuItem("转化为量化的灰度图");
		JMenuItem item14 = new JMenuItem("马尔可夫网络算法降噪");
		JMenuItem item15 = new JMenuItem("图像渐变转换");
		JMenuItem item16 = new JMenuItem("Display Histogram");
		JMenuItem item17 = new JMenuItem("Histogram Equalization");
		JMenuItem item18 = new JMenuItem("Show Patches");
		JMenuItem item19 = new JMenuItem("Averaging Filter(No Weighting)平滑");
		JMenuItem item20 = new JMenuItem("Median Filter");
		JMenuItem item21 = new JMenuItem("Max Filter");
		JMenuItem item22 = new JMenuItem("Min Filter");
		JMenuItem item23 = new JMenuItem("Laplacian Filter(a)锐化");
		JMenuItem item24 = new JMenuItem("Laplacian Filter(b)锐化");
		JMenuItem item25 = new JMenuItem("Laplacian Filter(c)锐化");
		JMenuItem item26 = new JMenuItem("Laplacian Filter(d)锐化");
		JMenuItem item27 = new JMenuItem("Sobel Filter(d)边缘检测");
		JMenuItem item28 = new JMenuItem("Sobel Filter(e)边缘检测");
		JMenuItem item29 = new JMenuItem("Robert Filter(b)边缘检测");
		JMenuItem item30 = new JMenuItem("Robert Filter(c)边缘检测");
		JMenuItem item31 = new JMenuItem("均值滤波降噪");
		JMenuItem item32 = new JMenuItem("中值滤波降噪");
		JMenuItem item33 = new JMenuItem("小波降噪");
		JMenuItem item34 = new JMenuItem("添加椒盐噪声");
		JMenuItem item35 = new JMenuItem("计算两张图像相似度");
		JMenuItem item36 = new JMenuItem("DFT");
		JMenuItem item37 = new JMenuItem("IDFT");
		JMenuItem item38 = new JMenuItem("FFT");
		JMenuItem item39 = new JMenuItem("IFFT");
		JMenuItem item40 = new JMenuItem("Averaging Filter");
		JMenuItem item41 = new JMenuItem("Laplacian Filter");
		JMenuItem item42 = new JMenuItem("Arithmetic Mean Filter");
		JMenuItem item43 = new JMenuItem("Harmonic Mean Filter");
		JMenuItem item44 = new JMenuItem("Contra-harmonic Mean Filter");
		JMenuItem item45 = new JMenuItem("添加高斯噪声");
		JMenuItem item46 = new JMenuItem("Geometric Mean Filter");
		JMenuItem item47 = new JMenuItem("HE(HW4 2.4.2)");
		JMenuItem item48 = new JMenuItem("Without Soft Matting");
		JMenuItem item49 = new JMenuItem("Soft Matting");
		JMenuItem item50 = new JMenuItem("Guided Image Filtering");
		JMenuItem item51 = new JMenuItem("透射率图(基本操作)");
		JMenuItem item52 = new JMenuItem("透射率图（导向滤波）");
		JMenuItem item53 = new JMenuItem("图像旋转");
		JMenuItem item54 = new JMenuItem("图像翻转");
		//JMenuItem item55 = new JMenuItem("动画播放");
		// 在不同的菜单组件中添加不同的菜单项
		menu1.add(item1);
		menu1.add(item2);
		menu1.addSeparator();
		menu1.add(item3);
		menu2.add(item4);
		menu2.add(item5);
		menu2.addSeparator();
		menu2.add(item6);
		menu2.add(item7);
		menu2.add(item8);
		menu2.addSeparator();
		menu2.add(item15);
		menu2.addSeparator();
		menu2.add(item35);
		menu2.addSeparator();
		menu2.add(item53);
		menu2.add(item54);
		menu3.add(item9);
		menu4.add(item10);
		menu4.add(item11);
		menu4.add(item12);
		menu5.add(item13);
		menu6.add(item34);
		menu6.add(item45);
		menu6.addSeparator();
		menu6.add(item31);
		menu6.add(item32);
		menu6.add(item33);
		menu6.addSeparator();
		menu6.add(item14);
		menu7.add(item16);
		menu7.add(item17);
		menu7.add(item47);
		menu8.add(item18);
		menu8.addSeparator();
		menu8.add(item19);
		menu8.addSeparator();
		menu8.add(item20);
		menu8.add(item21);
		menu8.add(item22);
		menu8.addSeparator();
		menu8.add(item23);
		menu8.add(item24);
		menu8.add(item25);
		menu8.add(item26);
		menu8.addSeparator();
		menu8.add(item29);
		menu8.add(item30);
		menu8.add(item27);
		menu8.add(item28);
		menu8.addSeparator();
		menu8.add(item42);
		menu8.add(item46);
		menu8.add(item43);
		menu8.add(item44);
		menu9.add(item36);
		menu9.add(item37);
		menu9.add(item38);
		menu9.add(item39);
		menu9.addSeparator();
		menu9.add(item40);
		menu9.add(item41);
		menu10.add(item48);
		menu10.add(item51);
		menu10.addSeparator();
		menu10.add(item49);
		menu10.addSeparator();
		menu10.add(item50);
		menu10.add(item52);
		//menu11.add(item55);

		// 设置窗口固定大小
		this.setSize(WIDTH, HEIGHT);
		// 设置窗口位置
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		int x = (width - WIDTH) / 2;
		int y = (height - HEIGHT) / 2;
		this.setLocation(x, y);
		// 设置可视化组件可见
		this.setVisible(true);

		// 设置退出事件
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 处理菜单项的动作事件
		// 处理File菜单项的动作事件，鼠标点击打开图片
		item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				image = read();
				showImage(image);
			}
		});

		// 处理Save菜单项的动作事件，鼠标点击保存图片
		item2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					write(image);
				}
			}
		});

		// 处理Exit菜单项的动作事件，鼠标点击退出程序
		item3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// 处理Turn To Gray Image菜单项的动作事件，将24位真彩图转化为灰度图显示出来
		item4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 对已有图片作灰度化处理
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.showGray(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理Turn To B&W Image菜单项的动作事件，将灰度图转化为黑白图显示出来
		item5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 对已有图片作黑白化处理
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.showBW(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理Show Red Component菜单项的动作事件，显示图像的红色通道
		item6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 对已有图片用红色滤镜处理
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.showChanelR(image);
					showImage(image);
				}
			}
		});

		// 处理Show Green Component菜单项的动作事件，显示图像的绿色通道
		item7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 对已有图片用绿色滤镜处理
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.showChanelG(image);
					showImage(image);
				}
			}
		});

		// 处理Show Blue Component菜单项的动作事件，显示图像的蓝色通道
		item8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 对已有图片用蓝色滤镜处理
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.showChanelB(image);
					showImage(image);
				}
			}
		});

		// 处理About菜单项的动作事件，鼠标点击弹出对话框
		item9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Imageshop 1.0\n"
						+ "Copyright :Yangliu");
			}
		});

		// 处理"等距采样法放缩"菜单项的动作事件,鼠标点击显示文本输入框
		item10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 显示文本输入框部分
					JLabel widthLabel = new JLabel("width");
					JLabel heightLabel = new JLabel("height");
					String widthOriginal = Integer.toString(image
							.getWidth(null));
					String heightOriginal = Integer.toString(image
							.getHeight(null));
					JTextField widthField = new JTextField(widthOriginal, 10);
					JTextField heightField = new JTextField(heightOriginal, 10);
					JButton jb = new JButton("Confirm");
					jb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							ImageshopProcessor ip = new ImageshopProcessor();
							image = ip.scale(toBufferedImage(image),
									Integer.parseInt(widthField.getText()),
									Integer.parseInt(heightField.getText()));
							showImage(image);

							// 显示解释说明
							JOptionPane
									.showMessageDialog(
											null,
											"       最临近插值算法，这是一种最基本、最简单的图像缩放算法，"
													+ "\n"
													+ "效果也是最不好的，放大后的图像有很严重的马赛克，缩小后的图"
													+ "\n"
													+ "像有很严重的失真；效果不好的根源就是其简单的最临近插值方法"
													+ "\n"
													+ "引入了严重的图像失真，比如，当由目标图的坐标反推得到的源图"
													+ "\n"
													+ "的的坐标是一个浮点数的时候，采用了四舍五入的方法，直接采用"
													+ "\n"
													+ "了和这个浮点数最接近的象素的值，这种方法是很不科学的，当推"
													+ "\n"
													+ "得坐标值为 0.75的时候，不应该就简单的取为1，既然是0.75，比1"
													+ "\n"
													+ "要小0.25 ，比0要大0.75 ,那么目标象素值其实应该根据这个源图中"
													+ "\n"
													+ "虚拟的点四周的四个真实的点来按照一定的规律计算出来的，这样"
													+ "\n" + "才能达到更好的缩放效果。");
						}
					});
					JPanel jp = new JPanel();
					FlowLayout fl = new FlowLayout(20);
					jp.setLayout(fl);
					jp.add(widthLabel);
					jp.add(widthField);
					jp.add(heightLabel);
					jp.add(heightField);
					jp.add(jb);
					Container con = getContentPane();
					con.add(jp, BorderLayout.NORTH);
					setVisible(true);
				}
			}
		});

		// 处理"双线性差值放大与均值缩小"菜单项的动作事件,鼠标点击显示文本输入框
		item11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 显示文本输入框部分
					JLabel widthLabel = new JLabel("width");
					JLabel heightLabel = new JLabel("height");
					String widthOriginal = Integer.toString(image
							.getWidth(null));
					String heightOriginal = Integer.toString(image
							.getHeight(null));
					JTextField widthField = new JTextField(widthOriginal, 10);
					JTextField heightField = new JTextField(heightOriginal, 10);
					JButton jb = new JButton("Confirm");
					jb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							ImageshopProcessor ip = new ImageshopProcessor();
							image = ip.scale2(toBufferedImage(image),
									Integer.parseInt(widthField.getText()),
									Integer.parseInt(heightField.getText()));
							showImage(image);

							// 显示解释说明
							JOptionPane.showMessageDialog(null,
									"      图像的双线性插值放大算法中，目标图像中新创造的象素值，" + "\n"
											+ "是由源图像位置在它附近的2*2区域4个邻近象素的值通过加权平"
											+ "\n"
											+ "均计算得出的。双线性内插值算法放大后的图像质量较高，不会"
											+ "\n"
											+ "出现像素值不连续的的情况。然而次算法具有低通滤波器的性质，"
											+ "\n"
											+ "使高频分量受损，所以可能会使图像轮廓在一定程度上变得模糊。"
											+ "\n"
											+ "也就是说具有抗锯齿功能, 但没有考虑边缘和图像的梯度变化，相比"
											+ "\n" + "之下双立方插值算法更好解决这些问题。");
						}
					});
					JPanel jp = new JPanel();
					FlowLayout fl = new FlowLayout(20);
					jp.setLayout(fl);
					jp.add(widthLabel);
					jp.add(widthField);
					jp.add(heightLabel);
					jp.add(heightField);
					jp.add(jb);
					Container con = getContentPane();
					con.add(jp, BorderLayout.NORTH);
					setVisible(true);
				}
			}
		});

		// 处理"双立方插值算法放缩"菜单项的动作事件,鼠标点击显示文本输入框
		item12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 显示文本输入框部分
					JLabel widthLabel = new JLabel("width");
					JLabel heightLabel = new JLabel("height");
					String widthOriginal = Integer.toString(image
							.getWidth(null));
					String heightOriginal = Integer.toString(image
							.getHeight(null));
					JTextField widthField = new JTextField(widthOriginal, 10);
					JTextField heightField = new JTextField(heightOriginal, 10);
					JButton jb = new JButton("Confirm");
					jb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							ImageshopProcessor ip = new ImageshopProcessor();
							image = ip.scale3(toBufferedImage(image),
									Integer.parseInt(widthField.getText()),
									Integer.parseInt(heightField.getText()));
							showImage(image);

							// 显示解释说明
							JOptionPane.showMessageDialog(null,
									"      双立方插值在图像放大过程可以保留更多的图像细节，放" + "\n"
											+ "大以后的图像带有反锯齿的功能，同时图像和源图像相比效果"
											+ "\n"
											+ "更加真实, 缺点是计算量比较大，是常见的三种图像放大算法"
											+ "\n"
											+ "中计算量最大的一种，据说Photoshop的图像放大就是基本双"
											+ "\n" + "立方插值的优化算法。");
						}
					});
					JPanel jp = new JPanel();
					FlowLayout fl = new FlowLayout(20);
					jp.setLayout(fl);
					jp.add(widthLabel);
					jp.add(widthField);
					jp.add(heightLabel);
					jp.add(heightField);
					jp.add(jb);
					Container con = getContentPane();
					con.add(jp, BorderLayout.NORTH);
					setVisible(true);
				}
			}
		});

		// 处理"灰度图量化"菜单项的动作事件,鼠标点击显示文本输入框
		item13.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 显示文本输入框部分
					JLabel levelLabel = new JLabel("level");
					JTextField levelField = new JTextField(10);
					JButton jb = new JButton("Confirm");
					jb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (Integer.parseInt(levelField.getText()) < 0
									|| Integer.parseInt(levelField.getText()) > 255) {
								JOptionPane.showMessageDialog(null,
										"'level' should be an integer in [0, 255] defining the number"
												+ "\n"
												+ "of gray levels of output.");
							} else {
								ImageshopProcessor ip = new ImageshopProcessor();
								image = ip.quantize(toBufferedImage(image),
										Integer.parseInt(levelField.getText()));
								showImage(image);
							}
						}
					});
					JPanel jp = new JPanel();
					FlowLayout fl = new FlowLayout(20);
					jp.setLayout(fl);
					jp.add(levelLabel);
					jp.add(levelField);
					jp.add(jb);
					Container con = getContentPane();
					con.add(jp, BorderLayout.NORTH);
					setVisible(true);
				}
			}
		});

		// 处理"马尔可夫网络算法降噪"菜单项的动作事件
		item14.addActionListener(MarkovDenoise);

		// 处理"图像渐变转换"菜单项的动作事件
		item15.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 设置比例初始值
				proportion = 0;

				JButton jb1 = new JButton("读入第一张图片");
				jb1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						imageFirst = read();
					}
				});
				JButton jb2 = new JButton("读入第二张图片");
				jb2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						imageSecond = read();
					}
				});
				JButton jb3 = new JButton("Confirm");
				jb3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (imageFirst == null) {
							JOptionPane.showMessageDialog(null, "请读入第一张图片！");
						} else if (imageSecond == null) {
							JOptionPane.showMessageDialog(null, "请读入第二张图片！");
						} else if (imageFirst != null && imageSecond != null) {
							// 调整图像大小到合适大小
							if (imageFirst.getWidth(null) != 400
									|| imageFirst.getHeight(null) != 400) {
								ImageshopProcessor ip = new ImageshopProcessor();
								imageFirst = ip.scale2(
										toBufferedImage(imageFirst), 400, 400);
							}
							if (imageSecond.getWidth(null) != 400
									|| imageSecond.getHeight(null) != 400) {
								ImageshopProcessor ip = new ImageshopProcessor();
								imageSecond = ip.scale2(
										toBufferedImage(imageSecond), 400, 400);
							}

							// 一开始显示第一张图像
							Container con = getContentPane();
							con.removeAll();
							JPanel jp = new JPanel();
							setContentPane(jp);
							BorderLayout bl = new BorderLayout();
							jp.setLayout(bl);
							sliderChange();
							// 添加滑块到窗口
							JSlider js = new JSlider(0, 100, 0);
							js.setPaintLabels(true);
							js.addChangeListener(new ChangeListener() {
								@Override
								public void stateChanged(ChangeEvent e) {
									proportion = js.getValue();
									sliderChange();
								}
							});
							jp.add(js, BorderLayout.SOUTH);
							setVisible(true);
						}
					}
				});
				Container con = getContentPane();
				con.removeAll();
				JPanel jp = new JPanel();
				setContentPane(jp);
				jp.add(jb1);
				jp.add(jb2);
				jp.add(jb3);
				SpringLayout sl = new SpringLayout();
				jp.setLayout(sl);
				sl.putConstraint(SpringLayout.NORTH, jb1, 140,
						SpringLayout.NORTH, jp);
				sl.putConstraint(SpringLayout.WEST, jb1, 180,
						SpringLayout.WEST, jp);
				sl.putConstraint(SpringLayout.NORTH, jb2, 200,
						SpringLayout.NORTH, jp);
				sl.putConstraint(SpringLayout.WEST, jb2, 180,
						SpringLayout.WEST, jp);
				sl.putConstraint(SpringLayout.NORTH, jb3, 260,
						SpringLayout.NORTH, jp);
				sl.putConstraint(SpringLayout.WEST, jb3, 200,
						SpringLayout.WEST, jp);
				setVisible(true);
			}
		});

		// 处理"Display Histogram"菜单项的动作事件
		item16.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					ip.showHist(toBufferedImage(image));
				}
			}
		});

		// 处理"Histogram Equalization"菜单项的动作事件
		item17.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.histogramEqualization(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"HE(HW4 2.4.2)"菜单项的动作事件
		item47.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.histogramEqualizationHW4(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"Show Patches"菜单项的动作事件
		item18.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 显示文本输入框部分
					JLabel widthLabel = new JLabel("width of patch");
					JLabel heightLabel = new JLabel("height of patch");
					JTextField widthField = new JTextField(10);
					JTextField heightField = new JTextField(10);
					JButton jb = new JButton("Confirm");
					jb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int width = Integer.parseInt(widthField.getText());
							int height = Integer.parseInt(heightField.getText());
							int[][][] arr_patch = new int[8][height][width];

							ImageshopProcessor ip = new ImageshopProcessor();
							arr_patch = ip.view_as_window(
									toBufferedImage(image), width, height);
							showPathes(arr_patch);
						}
					});
					JPanel jp = new JPanel();
					FlowLayout fl = new FlowLayout(20);
					jp.setLayout(fl);
					jp.add(widthLabel);
					jp.add(widthField);
					jp.add(heightLabel);
					jp.add(heightField);
					jp.add(jb);
					Container con = getContentPane();
					con.add(jp, BorderLayout.NORTH);
					setVisible(true);
				}
			}
		});

		// 处理"Averaging Filter"菜单项的动作事件
		item19.addActionListener(averageFilterListener);

		// 处理"Median Filter"菜单项的动作事件
		item20.addActionListener(medianFilterListener);

		// 处理"Max Filter"菜单项的动作事件
		item21.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 显示文本输入框部分
					JLabel widthLabel = new JLabel("width of patch");
					JLabel heightLabel = new JLabel("height of patch");
					JTextField widthField = new JTextField(10);
					JTextField heightField = new JTextField(10);
					JButton jb = new JButton("Confirm");
					jb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int width = Integer.parseInt(widthField.getText());
							int height = Integer.parseInt(heightField.getText());

							ImageshopProcessor ip = new ImageshopProcessor();
							image = ip.maxFilter(toBufferedImage(image), width,
									height);
							showImage(image);
						}
					});
					JPanel jp = new JPanel();
					FlowLayout fl = new FlowLayout(20);
					jp.setLayout(fl);
					jp.add(widthLabel);
					jp.add(widthField);
					jp.add(heightLabel);
					jp.add(heightField);
					jp.add(jb);
					Container con = getContentPane();
					con.add(jp, BorderLayout.NORTH);
					setVisible(true);
				}
			}
		});

		// 处理"Min Filter"菜单项的动作事件
		item22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					// 显示文本输入框部分
					JLabel widthLabel = new JLabel("width of patch");
					JLabel heightLabel = new JLabel("height of patch");
					JTextField widthField = new JTextField(10);
					JTextField heightField = new JTextField(10);
					JButton jb = new JButton("Confirm");
					jb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int width = Integer.parseInt(widthField.getText());
							int height = Integer.parseInt(heightField.getText());

							ImageshopProcessor ip = new ImageshopProcessor();
							image = ip.minFilter(toBufferedImage(image), width,
									height);
							showImage(image);
						}
					});
					JPanel jp = new JPanel();
					FlowLayout fl = new FlowLayout(20);
					jp.setLayout(fl);
					jp.add(widthLabel);
					jp.add(widthField);
					jp.add(heightLabel);
					jp.add(heightField);
					jp.add(jb);
					Container con = getContentPane();
					con.add(jp, BorderLayout.NORTH);
					setVisible(true);
				}
			}
		});

		// 处理"Laplacian Filter(a)"菜单项的动作事件
		item23.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.laplacianFilterA(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"Laplacian Filter(b)"菜单项的动作事件
		item24.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.laplacianFilterB(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"Laplacian Filter(c)"菜单项的动作事件
		item25.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.laplacianFilterC(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"Laplacian Filter(d)"菜单项的动作事件
		item26.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.laplacianFilterD(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"Sobel Filter(d)"菜单项的动作事件
		item27.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.sobelFilterD(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"Sobel Filter(e)"菜单项的动作事件
		item28.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.sobelFilterE(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"Robort Filter(b)"菜单项的动作事件
		item29.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.robortFilterB(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"Robort Filter(c)"菜单项的动作事件
		item30.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否已经有要保存的图片
				if (image == null) {
					JOptionPane.showMessageDialog(null,
							"You should open a photo first!");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.robortFilterC(toBufferedImage(image));
					showImage(image);
				}
			}
		});

		// 处理"随机产生椒盐噪声"菜单项的动作事件
		item34.addActionListener(noiseSP);

		// 处理"均值滤波降噪"菜单项的动作事件
		item31.addActionListener(averageFilterListener);

		// 处理"中值滤波降噪"菜单项的动作事件
		item32.addActionListener(medianFilterListener);

		// 处理"计算两张图像相似度"菜单项的动作事件
		item35.addActionListener(compareListener);

		// 处理"DFT"菜单项的动作事件
		item36.addActionListener(DFTListener);

		// 处理"IDFT"菜单项的动作事件
		item37.addActionListener(IDFTListener);

		// 处理"FFT"菜单项的动作事件
		item38.addActionListener(FFTListener);

		// 处理"IFFT"菜单项的动作事件
		item39.addActionListener(IFFTListener);

		// 处理"Frequency Domain"菜单中"Averaging Filter"菜单项的动作事件
		item40.addActionListener(affListener);

		// 处理"Frequency Domain"菜单中"Laplacian Filter"菜单项的动作事件
		item41.addActionListener(lffListener);

		// 处理"Spatial Filter"菜单中"Arithmetic Mean Filter"菜单项的动作事件
		item42.addActionListener(amfListener);

		// 处理"Spatial Filter"菜单中"Harmonic Mean Filter"菜单项的动作事件
		item43.addActionListener(hmfListener);

		// 处理"Spatial Filter"菜单中"Contra-harmonic Mean Filter"菜单项的动作事件
		item44.addActionListener(cmfListener);

		// 处理"Denoise"菜单中"添加高斯噪声"菜单项的动作事件
		item45.addActionListener(noiseGaussian);

		// 处理"Spatial Filter"菜单中"Geometric Mean Filter"菜单项的动作事件
		item46.addActionListener(gmfListener);

		// 处理"Haze Removal"菜单中"Without Soft Matting"菜单项的动作事件
		item48.addActionListener(WSMListener);

		// 处理"Haze Removal"菜单中"Soft Matting"菜单项的动作事件
		item49.addActionListener(SMListener);

		// 处理"Haze Removal"菜单中"Guided Image Filtering"菜单项的动作事件
		item50.addActionListener(GIFListener);

		// 处理"Haze Removal"菜单中"透射率图(基本操作)"菜单项的动作事件
		item51.addActionListener(BTListener);

		// 处理"Haze Removal"菜单中"透射率图(导向滤波)"菜单项的动作事件
		item52.addActionListener(GIFTListener);

		// 处理“Process”菜单中“图像旋转”菜单项的动作事件
		item53.addActionListener(rotateListener);
		
		// 处理“Process”菜单中“图像翻转”菜单项的动作事件
		item54.addActionListener(flipListener);
		
		// 处理“动画播放”菜单中“动画播放”菜单项的动作事件
		//item55.addActionListener(flashListener);
	}

	// "Averaging Filter"菜单项的动作监听函数
	ActionListener averageFilterListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.averagingFilter(toBufferedImage(image),
								width, height);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// "Median Filter"菜单项的动作监听函数
	ActionListener medianFilterListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.medianFilter(toBufferedImage(image), width,
								height);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// "添加椒盐噪声"菜单项的动作监听函数
	ActionListener noiseSP = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel saltLabel = new JLabel("salt噪声概率");
				JLabel pepperLabel = new JLabel("pepper噪声概率");
				JTextField saltField = new JTextField(10);
				JTextField pepperField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						double sp = Double.parseDouble(saltField.getText());
						double pp = Double.parseDouble(pepperField.getText());

						if (sp == 0 && pp == 0) {
							JOptionPane.showMessageDialog(null, "图像没有加入噪声");
						} else {
							ImageshopProcessor ip = new ImageshopProcessor();
							image = ip.makeNoiseSP(toBufferedImage(image), sp,
									pp);
							showImage(image);
						}
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(saltLabel);
				jp.add(saltField);
				jp.add(pepperLabel);
				jp.add(pepperField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// "添加高斯噪声"菜单项的动作监听函数
	ActionListener noiseGaussian = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel meanLabel = new JLabel("Mean");
				JLabel varianceLabel = new JLabel("Standard variance");
				JTextField meanField = new JTextField(10);
				JTextField varianceField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						double mean = Double.parseDouble(meanField.getText());
						double variance = Double.parseDouble(varianceField
								.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.makeNoiseGaussian(toBufferedImage(image),
								mean, variance);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(meanLabel);
				jp.add(meanField);
				jp.add(varianceLabel);
				jp.add(varianceField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// "马尔可夫网络算法降噪"菜单项的动作监听函数
	ActionListener MarkovDenoise = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				if (toBufferedImage(image).getType() != BufferedImage.TYPE_BYTE_BINARY) {
					JOptionPane.showMessageDialog(null, "此操作只能对二值图像进行处理。");
				} else {
					ImageshopProcessor ip = new ImageshopProcessor();
					image = ip.MarkovDenoise(toBufferedImage(image));
					showImage(image);
				}
			}
		}
	};

	// "计算两张图像相似度"菜单项的动作监听函数
	ActionListener compareListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JButton jb1 = new JButton("读入第一张图片");
			jb1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					imageFirst = read();
				}
			});
			JButton jb2 = new JButton("读入第二张图片");
			jb2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					imageSecond = read();
				}
			});
			JButton jb3 = new JButton("Confirm");
			jb3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (imageFirst == null) {
						JOptionPane.showMessageDialog(null, "请读入第一张图片！");
					} else if (imageSecond == null) {
						JOptionPane.showMessageDialog(null, "请读入第二张图片！");
					} else if (imageFirst != null && imageSecond != null) {
						// 调整图像大小到合适大小
						if (imageFirst.getWidth(null) != imageSecond
								.getWidth(null)
								|| imageFirst.getHeight(null) != imageSecond
										.getHeight(null)) {
							JOptionPane
									.showMessageDialog(null, "请读入两张大小一致的图片！");
						} else {
							ImageshopProcessor ip = new ImageshopProcessor();
							double similarity = ip.computeSimilarity(
									toBufferedImage(imageFirst),
									toBufferedImage(imageSecond));
							JOptionPane.showMessageDialog(null, "两张图片的相似度为"
									+ similarity + "%");
						}
					}
				}
			});
			Container con = getContentPane();
			con.removeAll();
			JPanel jp = new JPanel();
			setContentPane(jp);
			jp.add(jb1);
			jp.add(jb2);
			jp.add(jb3);
			SpringLayout sl = new SpringLayout();
			jp.setLayout(sl);
			sl.putConstraint(SpringLayout.NORTH, jb1, 140, SpringLayout.NORTH,
					jp);
			sl.putConstraint(SpringLayout.WEST, jb1, 180, SpringLayout.WEST, jp);
			sl.putConstraint(SpringLayout.NORTH, jb2, 200, SpringLayout.NORTH,
					jp);
			sl.putConstraint(SpringLayout.WEST, jb2, 180, SpringLayout.WEST, jp);
			sl.putConstraint(SpringLayout.NORTH, jb3, 260, SpringLayout.NORTH,
					jp);
			sl.putConstraint(SpringLayout.WEST, jb3, 200, SpringLayout.WEST, jp);
			setVisible(true);
		}
	};

	// "DFT"菜单项的动作监听函数
	ActionListener DFTListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				ImageshopProcessor ip = new ImageshopProcessor();
				image = ip.DFT(toBufferedImage(image));
				showImage(image);
			}
		}
	};

	// "IDFT"菜单项的动作监听函数
	ActionListener IDFTListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				ImageshopProcessor ip = new ImageshopProcessor();
				image = ip.IDFT(toBufferedImage(image));
				showImage(image);
			}
		}
	};

	// "FFT"菜单项的动作监听函数
	ActionListener FFTListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				ImageshopProcessor ip = new ImageshopProcessor();
				image = ip.FFT(toBufferedImage(image));
				showImage(image);
			}
		}
	};

	// "IFFT"菜单项的动作监听函数
	ActionListener IFFTListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				ImageshopProcessor ip = new ImageshopProcessor();
				image = ip.IFFT(toBufferedImage(image));
				showImage(image);
			}
		}
	};

	// "Frequency Domain"菜单中"Averaging Filter"菜单项的动作监听函数
	ActionListener affListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				ImageshopProcessor ip = new ImageshopProcessor();
				image = ip.averagingFilterFrequency(toBufferedImage(image));
				showImage(image);
			}
		}
	};

	// "Frequency Domain"菜单中"Laplacian Filter"菜单项的动作监听函数
	ActionListener lffListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				ImageshopProcessor ip = new ImageshopProcessor();
				image = ip.laplacianFilterFrequency(toBufferedImage(image));
				showImage(image);
			}
		}
	};

	// "Spatial Filter"菜单中"Arithmetic Mean Filter"菜单项的动作监听函数
	ActionListener amfListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.ArithmeticMeanFilter(toBufferedImage(image),
								width, height);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// "Spatial Filter"菜单中"Geometric Mean Filter"菜单项的动作监听函数
	ActionListener gmfListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.GeometricMeanFilter(toBufferedImage(image),
								width, height);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// "Spatial Filter"菜单中"Harmonic Mean Filter"菜单项的动作监听函数
	ActionListener hmfListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.HarmonicMeanFilter(toBufferedImage(image),
								width, height);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// "Spatial Filter"菜单中"Contra-harmonic Mean Filter"菜单项的动作监听函数
	ActionListener cmfListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JLabel QLabel = new JLabel("Q");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JTextField QField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());
						double Q = Double.parseDouble(QField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.ContraharmonicMeanFilter(
								toBufferedImage(image), width, height, Q);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(QLabel);
				jp.add(QField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// "Haze Removal"菜单中"Without Soft Matting"菜单项的动作监听函数
	ActionListener WSMListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.WSMHazeRemoval(toBufferedImage(image),
								width, height);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// "Haze Removal"菜单中"Soft Matting"菜单项的动作监听函数
	ActionListener SMListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TO DO！
			// 看不懂公式啊（数学老师死的早）。。于是去做Guided Image Filtering了
		}
	};

	// 处理"Haze Removal"菜单中"Guided Image Filtering"菜单项的动作监听函数
	ActionListener GIFListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.GIFHazeRemoval(toBufferedImage(image),
								width, height);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// 处理"Haze Removal"菜单中"透射率图(基本操作)"菜单项的动作监听函数
	ActionListener BTListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.BTransmission(toBufferedImage(image), width,
								height);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// 处理"Haze Removal"菜单中"透射率图(导向滤波)"菜单项的动作监听函数
	ActionListener GIFTListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				// 显示文本输入框部分
				JLabel widthLabel = new JLabel("width of patch");
				JLabel heightLabel = new JLabel("height of patch");
				JTextField widthField = new JTextField(10);
				JTextField heightField = new JTextField(10);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int width = Integer.parseInt(widthField.getText());
						int height = Integer.parseInt(heightField.getText());

						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.GIFTransmission(toBufferedImage(image),
								width, height);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(widthLabel);
				jp.add(widthField);
				jp.add(heightLabel);
				jp.add(heightField);
				jp.add(jb);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// 处理"Process”菜单中“图像旋转”菜单项的动作监听函数
	ActionListener rotateListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				JButton jb1 = new JButton("顺时针旋转90°");
				JButton jb2 = new JButton("逆时针旋转90°");
				jb1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.Rotate(toBufferedImage(image), 1);
						showImage(image);
					}
				});
				jb2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.Rotate(toBufferedImage(image), 2);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(jb1);
				jp.add(jb2);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};

	// 处理"Process”菜单中“图像翻转”菜单项的动作监听函数
	ActionListener flipListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 判断是否已经读入图片
			if (image == null) {
				JOptionPane.showMessageDialog(null,
						"You should open a photo first!");
			} else {
				JButton jb1 = new JButton("水平翻转");
				JButton jb2 = new JButton("竖直翻转");
				jb1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.Flip(toBufferedImage(image), 1);
						showImage(image);
					}
				});
				jb2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ImageshopProcessor ip = new ImageshopProcessor();
						image = ip.Flip(toBufferedImage(image), 2);
						showImage(image);
					}
				});
				JPanel jp = new JPanel();
				FlowLayout fl = new FlowLayout(20);
				jp.setLayout(fl);
				jp.add(jb1);
				jp.add(jb2);
				Container con = getContentPane();
				con.add(jp, BorderLayout.NORTH);
				setVisible(true);
			}
		}
	};
	
	// 处理"动画播放”菜单的动作监听函数
    /*
	ActionListener flashListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("IN!!!!!!!!");
			
			GridBagLayout layout = new GridBagLayout();
			GridBagConstraints constraints = new GridBagConstraints();
			FlashPlayer flashPlayer = new FlashPlayer();
			
			JPanel jp = new JPanel();
			jp.setLayout(layout);
			flashPlayer.setFlashFolder("src/Imageshop");
			flashPlayer.setFileName("水墨风.swf");
			flashPlayer.play();
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 1;
			constraints.weighty = 1;
			constraints.anchor = GridBagConstraints.CENTER;
			layout.setConstraints(flashPlayer.flashpanel, constraints);
			jp.add(flashPlayer.flashpanel);
			Container con = getContentPane();
			con.add(jp, BorderLayout.CENTER);
			setVisible(true);
		}
	};
    */
	
	/**
	 * 滑块改变时，显示新混合的图像
	 */
	private void sliderChange() {
		ImageshopProcessor ip = new ImageshopProcessor();
		image = ip.slowChange(toBufferedImage(imageFirst),
				toBufferedImage(imageSecond), proportion);
		ImagePanel ipanel = new ImagePanel(image, WIDTH);
		// 将显示图片的panel加入到frame中

		Container con = getContentPane();
		con.add(ipanel, BorderLayout.CENTER);
		// 设置可视化组件可见
		setVisible(true);
	}

	/**
	 * 读入图片，返回读入的图片变量
	 * 
	 * @return
	 */
	private Image read() {
		// 以用户桌面为打开文件对话框的默认路径
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// 将桌面的那个文件目录赋值给file
		File userFile = fsv.getHomeDirectory();
		// 输出桌面那个目录的路径
		JFileChooser fileChooser = new JFileChooser(userFile.getPath());
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		int result = fileChooser.showDialog(null, "打开图片");
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			ImageshopIO ii = new ImageshopIO();
			Image imageTmp = ii.imageRead(file.getAbsolutePath());
			return imageTmp;
		}

		return (Image) null;
	}

	/**
	 * 将imageTmp中的图片存储到相应路径
	 * 
	 * @param imageTmp
	 */
	private void write(Image imageTmp) {
		// 以用户桌面为打开文件对话框的默认路径
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// 将桌面的那个文件目录赋值给file
		File userFile = fsv.getHomeDirectory();
		// 输出桌面那个目录的路径
		JFileChooser fileChooser = new JFileChooser(userFile.getPath());
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		int result = fileChooser.showDialog(null, "保存图片");
		if (result == JFileChooser.APPROVE_OPTION) {
			// 获取保存图片路径
			File file = fileChooser.getSelectedFile();
			// 创建ImageIO类，调用imageWrite保存图片
			ImageshopIO ii = new ImageshopIO();
			if (ii.imageWrite(toBufferedImage(imageTmp), file.getAbsolutePath()) != null) {
				JOptionPane.showMessageDialog(null, "Save successfully!");
			}
		}
	}

	/**
	 * 将保存为私有成员变量的图片显示在窗口内
	 */
	private void showImage(Image imageTmp) {
		ImagePanel ipanel = new ImagePanel(imageTmp, WIDTH);
		// 将显示图片的panel加入到frame中
		Container con = getContentPane();
		// 清除之前显示的图片
		con.removeAll();
		con.add(ipanel, BorderLayout.CENTER);
		// 设置可视化组件可见
		setVisible(true);
	}

	/**
	 * 将随机选出的8个patch按一定格式写到txt中
	 * 
	 * @param arr_patch
	 */
	private void showPathes(int[][][] arr_patch) {
		try {
			File file = new File("C:/Users/Austin/Desktop/patches.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file, true);
			PrintStream p = new PrintStream(out);

			p.println("这是随机选出的8个patches的例子：");
			for (int x = 0; x < 8; x++) {
				p.println("Patch " + (x + 1) + ":");
				for (int y = 0; y < arr_patch[x].length; y++) {
					for (int z = 0; z < arr_patch[x][y].length; z++) {
						p.print(arr_patch[x][y][z]);

						if (z == arr_patch[x][y].length - 1) {
							p.println();
						} else {
							p.print(" ");
						}
					}
				}

				if (x != 7) {
					p.println();
				}
			}

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Change Image to BufferedImage
	 * 
	 * @param image
	 * @return
	 */
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent
		// Pixels
		// boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			/*
			 * if (hasAlpha) { transparency = Transparency.BITMASK; }
			 */

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null),
					image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			// int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
			/*
			 * if (hasAlpha) { type = BufferedImage.TYPE_INT_ARGB; }
			 */
			bimage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}
}
