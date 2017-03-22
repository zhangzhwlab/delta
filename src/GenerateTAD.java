/********************************************************************
 * this used to create tad image according to the query scope and the picture width
 * @author lenovo
 *
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.lang.Math;

import javax.imageio.ImageIO;

public class GenerateTAD {
	
	private static float scale_cur=0.0001f;
	private static		BufferedImage bi = new BufferedImage(760, 260, BufferedImage.TYPE_INT_ARGB);
	
	private static Graphics2D ig2 = bi.createGraphics();
	public static void main(String []args){
		
		try{
			String srcpic="E://tad_chr7.png";
			getScale(760.0f,127229611.0f);
			ig2.setBackground(Color.white);
			ig2.setColor(Color.white);
			ig2.fillRect(0, 0, 760, 260);
			
			
			BufferedReader br= new BufferedReader(new FileReader("E://chr7_100kb.RAWobserved.tad.srt.mm.gff3"));
			String line="";
			br.readLine();
			int count =0 ;
			while((line = br.readLine()) != null){
				count ++;
				drawTAD(srcpic,line);
				System.out.println(count);
			}
			
			br.close();
			ig2.dispose();
			
			String dir = "E://tad7//";
			int maxscale = 1024;
			for(int i=2;i<=maxscale;i=i*2){
				
				File file = new File(dir+i);
				file.mkdirs();
				
				
				splitImage(srcpic,dir+i+"//",1,i);
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
	}
	
	
	
	public static void drawTAD(String srcpic,String line){
		
		String [] arrys = line.split("\\s+") ;
		int view_start = Integer.parseInt(arrys[3]);
		int view_end = Integer.parseInt(arrys[4]);
		
		String [] t_note = arrys[8].split(";");
		
		String note = t_note[2].substring(5,t_note[2].length());
		
		int target_start = 0 ;
		int target_end =0 ;
		
		int min_val=0;
		int max_val=0;
		
		String[] t_arry = note.split(":");
		String anchor_str = t_arry[0];
		String target_str = t_arry[1];
		
		int index1 = anchor_str.indexOf("-");
		int anchor_start = Integer.parseInt(anchor_str.substring(0, index1));
	
		int anchor_end = Integer.parseInt(anchor_str.substring(index1+1,anchor_str.length()));

		
		
		index1 = target_str.indexOf("-");
		target_start =Integer.parseInt( target_str.substring(0, index1) );
			
		target_end =Integer.parseInt( target_str.substring(index1+1, target_str.length()) );
		
		
		if(t_arry.length ==3){
			index1 = t_arry[2].indexOf("-");
			min_val = (int)(Float.parseFloat(t_arry[2].substring(0, index1) ));
				
			max_val = (int)(Float.parseFloat( t_arry[2].substring(index1+1, t_arry[2].length()))) ;
		}
		
		float boxstart ;
		float boxwidth ;
		float leftw ;
		float rightw;
		
		
		float cell_width= bpToX(anchor_end) - bpToX(anchor_start);
		float cell_height = bpToX(target_end)- bpToX(target_start);
		
		leftw = cell_width;
		rightw = cell_height;
		
		if(anchor_start >= target_start){
			view_start = target_start;
			view_end = anchor_end;
			
		}else{
			view_start = anchor_start ;
			view_end = target_end;
		}
		
		
		boxstart = bpToX(view_start);
		
		
		//boxwidth = total_width* Math.SQRT2;
		boxwidth = bpToX(view_end)-bpToX(view_start);
		
		
		
		//draw
		
		int anglescale=1;
		
		int qual_score = (int)(Float.parseFloat(arrys[5]) );
		
		
			
			String[] color_range = {"255,255,204","255,237,160","254,217,118","254,178,76","253,141,60","252,78,42","227,26,28","189,0,38","128,0,38"};
			
			if(max_val ==0){
				max_val =12;
			}
		
			int tc = (qual_score -min_val)*8/(max_val-min_val);
		//	System.out.println("color range="+tc);
			String tc_color = color_range[tc];
			if(tc_color == ""){
				tc_color="255,255,255";
			}
		
		/* horizontal width of two mates
		the width is used as horizontal side of a isosceles
		*/
		// top corner point position
			
		double _tan=Math.tan(anglescale*Math.PI/4);
		double top_x = boxstart+boxwidth/2;
		//var top_y = yoffset+item.boxwidth*_tan/2;
		double top_y = boxwidth*_tan/2;
		
		//console.log("topx="+top_x+",topy="+top_y+",leftw="+leftw+",rightw="+rightw);
		
		
		//context.fillStyle = tc_color;
		//alert(color);
	//	context.beginPath();
		/*		p3
			p4		p2
				p1
		*/
		double a1=top_x;
		double	b1=top_y;
		double a2=(top_x+leftw/2);
		double	b2=(top_y-leftw*_tan/2);
		double	a3=(top_x+leftw/2-rightw/2);
		double	b3=(top_y-leftw*_tan/2-rightw*_tan/2);
		double	a4= (top_x-rightw/2);
		double	b4= (top_y-rightw*_tan/2);
		
		String [] c_arry = tc_color.split(",");

		
		
		ig2.setPaint(new Color(Integer.parseInt(c_arry[0]),Integer.parseInt(c_arry[1]),Integer.parseInt(c_arry[2])));
		//System.out.println(a1+" "+b1+" "+a2+" "+b2+" "+a3+" "+b3+" "+a4+" "+b4 );
	//	ig2.setPaint(Color.red) ;
		ig2.draw(new Line2D.Double(a1, b1, a2, b2));
		ig2.draw(new Line2D.Double(a2, b2, a3, b3));
		ig2.draw(new Line2D.Double(a3, b3, a4, b4));
		ig2.draw(new Line2D.Double(a4, b4, a1, b1));
			
		try{
			ImageIO.write(bi, "PNG", new File(srcpic));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	
	
	public static void splitImage(String srcpng,String dir,int rows,int cols){
		File file = new File(srcpng); 
		try{
			  FileInputStream fis = new FileInputStream(file);    
		      BufferedImage image = ImageIO.read(fis); //把文件读到图片缓冲流中  
              Image itemp = image.getScaledInstance(760, 260, bi.SCALE_SMOOTH);
    		  
		      AffineTransformOp op = new AffineTransformOp(AffineTransform
                      .getScaleInstance(1, cols), null);
		      
		      itemp = op.filter(image, null);
		     
		      int chunks = rows * cols;    
		  
		      int chunkWidth = image.getWidth() / cols; // 计算每一块小图片的高度和宽度   
		      int chunkHeight = image.getHeight() / rows;    
		      int count = 0;    
		      BufferedImage imgs[] = new BufferedImage[chunks];    
		      for (int x = 0; x < rows; x++) {    
		          for (int y = 0; y < cols; y++) {    
		              //初始化BufferedImage   
		        	 
		              imgs[count] = new BufferedImage(760, 260, BufferedImage.TYPE_INT_ARGB);    
		              
		              
		              //画出每一小块图片  
		              Graphics2D gr = imgs[count].createGraphics();  
		              gr.setBackground(Color.white);
		              gr.setColor(Color.white);
		              gr.fillRect(0, 0, 760, 260) ;
		              gr.drawImage(itemp, 0, 0, 760, 260, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);    
		              gr.dispose(); 
		              
 
				      count ++;
		          }    
		      }    
		      System.out.println("split picture file over");    
		  
		      //保存小图片到文件中  
		      for (int i = 0; i < imgs.length; i++) {    
		    	    ImageIO.write(imgs[i], "png", new File(dir + i + ".png")); 
		      }    
		      System.out.println("finish"); 
		}catch(Exception ex){
			ex.printStackTrace();
		}
	         

	}
	
	
	/*************************************
	 * compute each base's pixel
	 * @param width
	 * @param scope
	 * @return
	 */
	public static float bpToX(int bases){
		return scale_cur * bases;
	}
	
	
	public static void getScale(float width,float scope){
		scale_cur =  width/scope;
	}
}
