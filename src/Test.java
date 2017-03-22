import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



import cn.ac.big.circos.po.GFF3Format;
import cn.ac.big.circos.po.SpeciesBean;
import cn.ac.big.circos.util.ParseOutput;


public class Test {

	public static void main(String []args){
		/*String jsonpath="d:/human.gff3";	
		try{
			String chrom="1";
			
			ParseOutput pout = new ParseOutput();
			
			BufferedReader br = new BufferedReader(new FileReader(jsonpath));
			String line="";
			while(( line=br.readLine())!=null){
				if(line.startsWith(chrom)){
					GFF3Format gff = pout.parseGff3(line);
				int	chromlength = Integer.parseInt(gff.getEnd()) - Integer.parseInt(gff.getStart())+1;
					

				System.out.println(chromlength);
				break;
					
				}
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
		}*/
		//generateCoverage();
		//0,0,0;0.2123,0,0;0.1291,0.1493,0
		
		//double [] B=new double[]{0.0817,0.2200,0.0333};
		//double[] C= new double[]{-0.1491,0.2992,0.0599};
		
		//double[] A = new double[]{0.1291,0.1493,0};	
		double [] A = new double[]{0,0,0};
		double [] B = new double[]{0.2123,0,0};
		double [] C = new double[]{0.219,0.1493,0};
		
		double d = 1;
		
	
		/*double[] pa = get_panel(A,B,C);
		//求角平分线向量
		
		double []bi = get_bisectorVector(A,B,C);
		//System.out.println("get_bisectorVector "+bi[0]+","+bi[1]+","+bi[2]);
		
		//获取平面方程 A,B,C,D
		//
		double [] pi = get_panel(A,B,C); //平面方程 a,b,c
		//System.out.println("panel parameter "+pi[0]+","+pi[1]+","+pi[2]+","+pi[3]);
		
		//获取角平分线垂直线向量
		
		double [][] vi = get_vertical_biVector(bi[0],bi[1],bi[2],pi[0],pi[1],pi[2],pi[3],1);
		System.out.println("vertical bisector Vector = "+vi[0]+","+vi[1]+","+vi[2]);
		
		double [] fvi = null;
		if(vi!=null){
			for(int i=0;i<vi.length;i++){
				double re[] = vi[i];
				double check = re[0]*bi[0]+re[1]*bi[1]+re[2]*bi[2];
				double check1 = re[0]*re[0]+re[1]*re[1]+re[2]*re[2];
				//if(check1 >=1){
					System.out.println(re[0]+","+re[1]+","+re[2]);
					fvi = new double[]{re[0],re[1],re[2]};
					double R =0.0028251825182518254;
					
					double R1= R-0.001 ;
					double x = getPx(fvi[0],fvi[1],fvi[2],B[0],R1,1);
					double y = getPy(fvi[0],fvi[1],fvi[2],B[1],R1,1);
					double z = getPz(fvi[0],fvi[1],fvi[2],B[2],R1,1);
					System.out.println("start  = "+x+","+y+","+z);
					
				//	R= R1+0.001;
					x = getPx(fvi[0],fvi[1],fvi[2],B[0],R,1);
					y = getPy(fvi[0],fvi[1],fvi[2],B[1],R,1);
					z = getPz(fvi[0],fvi[1],fvi[2],B[2],R,1);
					
					
					double checkR = (x-B[0])*(x-B[0]) +(y-B[1])*(y-B[1])+(z-B[2])*(z-B[2]);
					System.out.println("check R ="+checkR +",R2="+R*R);
					
					
					System.out.println("end  = "+x+","+y+","+z);
					
				//}
				System.out.println("check vector, mo ="+check+","+check1);
			}
		}
		
		//double [] vi = get_vertical_biVector1(bi[0],bi[1],bi[2],pi[0],pi[1],pi[2],pi[3],B[0],B[1],B[2],1);
		//double checkmo = vi[0]*vi[0] + vi[1]*vi[1] +vi[2]*vi[2];
		//System.out.println("checkmo = "+checkmo);
		
		
		
		//double check = bi[0]*vi[0]+bi[1]*vi[1]+bi[2]*vi[2]; // vector
		//System.out.println("check vector ="+check);
		//double fangcheng = pi[0]*(vi[0]+B[0]) +pi[1]*(vi[1]+B[1])+pi[2]*(vi[2]+B[2])+pi[3] ;
		
		//System.out.println("fangchegn ="+fangcheng);
		
	//	double R=0.010340934093409342;
		
	//	double [] newvi = new double[]{vi[0],vi[1],vi[2]};*/
		
	    double [] newvi = get_vertical_biVector2(A[0],A[1],A[2],B[0],B[1],B[2],C[0],C[1],C[2]);
		double m = Math.sqrt(newvi[0]*newvi[0] + newvi[1]*newvi[1] + newvi[2]*newvi[2])  ;
		//double m = 20;
		//double m =1;
		System.out.println(newvi[0]+","+newvi[1]+","+newvi[2]);
		
		double R= 0.0018251825182518254;
		double R1= R+0.001 ;
		
		double x = B[0]+newvi[0]/m *R;
		double y = B[1]+newvi[1]/m *R;
		double z = B[2]+newvi[2]/m *R;
		
		double checkvector = (newvi[0]/m )*(newvi[0]/m )+(newvi[1]/m )*(newvi[1]/m )+(newvi[2]/m )*(newvi[2]/m );
		System.out.println("check vector="+checkvector);
		
		/*double x = getPx(newvi[0]/m,newvi[1]/m,newvi[2]/m,B[0],R,d);
		double y = getPy(newvi[0]/m,newvi[1]/m,newvi[2]/m,B[1],R,d);
		double z = getPz(newvi[0]/m,newvi[1]/m,newvi[2]/m,B[2],R,d);
		*/
		
		double mR = 0.05;
		System.out.println(-mR/(newvi[1]/m)+"<R<"+mR/(newvi[1]/m));
		
		double check = (x-B[0])*(x-B[0]) + (y-B[1])*(y-B[1]) +(z-B[2])*(z-B[2]);
		double Rval = R*R;
		System.out.println("check = "+check+","+ Rval);
		System.out.println("start  = "+x+","+y+","+z);
		
	//	R= R1+0.001;
		
		 x = B[0]+newvi[0]/m *R1;
		 y = B[1]+newvi[1]/m *R1;
		 z = B[2]+newvi[2]/m *R1;
		
		
		//x = getPx(newvi[0]/m,newvi[1]/m,newvi[2]/m,B[0],R1,d);
		//y = getPy(newvi[0]/m,newvi[1]/m,newvi[2]/m,B[1],R1,d);
		//z = getPz(newvi[0]/m,newvi[1]/m,newvi[2]/m,B[2],R1,d);
		System.out.println("end  = "+x+","+y+","+z);
		
		Rval = R1*R1 ;
		check = (x-B[0])*(x-B[0]) + (y-B[1])*(y-B[1]) +(z-B[2])*(z-B[2]);
		System.out.println("check = "+check+","+ Rval);
	
		
		
		//generateHic70();
		generateHic69();
	
	}
	
	//qiu pingmian  ax+by+cz+d=0
	public static double[] get_panel(double[] p1,double[] p2,double[] p3)  
	  
	{  
	    
	    double a = ( (p2[1]-p1[1])*(p3[2]-p1[2])-(p2[2]-p1[2])*(p3[1]-p1[1]) );  
	  
	    double b = ( (p2[2]-p1[2])*(p3[0]-p1[0])-(p2[0]-p1[0])*(p3[2]-p1[2]) );  
	  
	    double c = ( (p2[0]-p1[0])*(p3[1]-p1[1])-(p2[1]-p1[1])*(p3[0]-p1[0]) );  
	  
	    double d = ( 0-(a*p1[0]+b*p1[1]+c*p1[2]) );  
	    
	    double [] pa = new double[]{a,b,c,d};
	    return pa;
	  
	}  
	
	
	// 法向量
	public static  double[] get_FaVector(float[] p1,float[] p2,float[] p3)  
	  
	{  
	    
	    double a = ( (p2[1]-p1[1])*(p3[2]-p1[2])-(p2[2]-p1[2])*(p3[1]-p1[1]) );  
	  
	    double b = ( (p2[2]-p1[2])*(p3[0]-p1[0])-(p2[0]-p1[0])*(p3[2]-p1[2]) );  
	  
	    double c = ( (p2[0]-p1[0])*(p3[1]-p1[1])-(p2[1]-p1[1])*(p3[0]-p1[0]) );  
	    
	    double [] pa = new double[]{a,b,c};
	    return pa;
	  
	}  
	
	
	
	//获取圆内线交点 坐标
	public static double[] get_bisectorJiaoDian(float[] A, float[] B,float[]C){
		double [] d = null;
		double LAB = Math.sqrt( (A[0]-B[0])*(A[0]-B[0])+(A[1]-B[1])*(A[1]-B[1])+(A[2]-B[2])*(A[2]-B[2]));
		double LBC = Math.sqrt( (C[0]-B[0])*(C[0]-B[0])+(C[1]-B[1])*(C[1]-B[1])+(C[2]-B[2])*(C[2]-B[2]));
		double LAC = Math.sqrt( (C[0]-A[0])*(C[0]-A[0])+(C[1]-A[1])*(C[1]-A[1])+(C[2]-A[2])*(C[2]-A[2]));
		
		double m = (LAB*C[0] +LBC*A[0]+LAC*B[0])/(LBC+LAB+LAC);
		double n= (LAB*C[1] +LBC*A[1]+LAC*B[1])/(LBC+LAB+LAC);
		double p = (LAB*C[2] +LBC*A[2]+LAC*B[2])/(LBC+LAB+LAC);
		
		d= new double[]{m,n,p};
		return d;
	}
	
	
	//获取角平分线的向量, 其中假设B为角平分线定点
	public static double[] get_bisectorVector(double [] A, double [] B, double [] C){
		
		double [] d= null;
		double LAB = Math.sqrt( (A[0]-B[0])*(A[0]-B[0])+(A[1]-B[1])*(A[1]-B[1])+(A[2]-B[2])*(A[2]-B[2]));
		double LBC = Math.sqrt( (C[0]-B[0])*(C[0]-B[0])+(C[1]-B[1])*(C[1]-B[1])+(C[2]-B[2])*(C[2]-B[2]));
		double LAC = Math.sqrt( (C[0]-A[0])*(C[0]-A[0])+(C[1]-A[1])*(C[1]-A[1])+(C[2]-A[2])*(C[2]-A[2]));
		
		double v = (C[0]-B[0]) *(A[0]-B[0]) +(C[1]-B[1])*(A[1]-B[1])+(C[2]-B[2])*(C[2]-B[2]);
		double direction = 1;
		/*if(v <0){
			double m =  (-LAB*(C[0]-B[0])+LBC*(A[0]-B[0]) )/LAB*LBC ;
			double n = (-LAB*(C[1]-B[1])+LBC*(A[1]-B[1]))/LAB*LBC;
			double p = (-LAB*(C[2]-B[2])+LBC*(A[2]-B[2]))/LAB*LBC ;
			direction = -1;
			d= new double[]{m,n,p,direction};
		}else {*/
			double m =  (LAB*(C[0]-B[0])+LBC*(A[0]-B[0]) )/LAB*LBC ;
			double n = (LAB*(C[1]-B[1])+LBC*(A[1]-B[1]))/LAB*LBC;
			double p = (LAB*(C[2]-B[2])+LBC*(A[2]-B[2]))/LAB*LBC ;
			d= new double[]{m,n,p,direction};
		//}	
		
		
		return d;
	}
	
	
	
	

	
	
	
	
	/*********************************************************
	 * 获取垂直面的方程
	 * @param A   三角形某点
	 * @param B   内心交点
	 * @param C   平面向量
	 * @return
	 */
	public static double[] get_verticalPanel(float[]A,double[]B,double[]C,double d){
		double f[] = null;
		double c = d*C[0]*((C[1]*B[0]-C[0]*B[1])-(C[1]*A[0]-C[0]*A[1]))/(C[2]*A[0]-C[0]*A[2])*(C[1]*B[0]-C[0]*B[1])-(C[2]*B[0]-C[0]*B[2])*(C[1]*A[0]-C[0]*A[1]);
		double b = d*C[0]/(C[1]*A[0]-C[0]*A[1])-(C[2]*A[0]-C[0]*A[2])/(C[1]*A[0]-C[0]*A[1])*c;
		double a = -C[2]/C[0] *c - C[1]/C[0]*b;
		if(Double.isNaN(c)){
			c=0;
		}
		if(Double.isNaN(b)){
			b=0;
			
		}
		if(Double.isNaN(a)){
		a=0;
		}
		f= new double[]{a,b,c};
		return f;
	}
	
	
	/*************************************************
	 * double d
	 * 求取垂直于角平分线的空间直线向量 x,y,z
	 * @return
	 */
	
	public static double [][] get_vertical_biVector(double m,double n,double p,double A,double B,double C, double d,double Direction){
		double [][]re = new double[8][];
		double xishu = 1;
		double a = m*m*(A*n-B*m)*(A*n-B*m)+(B*m*p-n*C*m)*(B*m*p-n*C*m)+(A*p-C*m)*(A*p-C*m)*m*m;
		double b = -2*(B*m*p-n*C*m)*n*m*d-2*m*m*m*d*(A*p-C*m);
		double c = n*n*m*m*d*d + m*m*d*d*m*m - xishu*(A*n-B*m)*(A*n-B*m)*m*m ;
		
		double x =0 ;
		double y = 0 ;
		double z = 0 ;
		double x1=0;
		double y1= 0 ;
		double z1= 0 ;
		double delta = b*b - 4*a*c;
		double fenmu = A*n - B*m ;
		System.out.println("delta="+delta);
		double check = 0 ;
		if(a !=0  && fenmu !=0){
			
			if(delta >= 0 ){
				//if(Direction >0){
					z = (-b +Math.sqrt(delta))/2*a;
					
					
					a = m*m+n*n;
					b = 2*n*p*z;
					c = z*z*p*p-(1*xishu-z*z)*m*m;
					delta = b*b -4*a*c;
					if(a != 0 && delta >=0){
						y = (-b +Math.sqrt(delta))/2*a;
						x = Math.sqrt(1*xishu-y*y-z*z);
						check = x*x + y*y +z*z;
						System.out.println("checkxyz="+check);
						re[0] = new double[]{x,y,z};
						x1 = -Math.sqrt(1*xishu-y*y-z*z);
						check = x1*x1 + y*y +z*z;
						System.out.println("checkxyz="+check);
						re[1] = new double[]{x1,y,z};
						y1 = (-b -Math.sqrt(delta))/2*a;
						
						x = Math.sqrt(1*xishu-y1*y1-z*z);
						check = x*x + y1*y1 +z*z;
						re[2] = new double[]{x,y1,z};
						System.out.println("checkxyz="+check);
						
						x1 = -Math.sqrt(1*xishu-y1*y1-z*z);
						re[3] = new double[]{x1,y1,z};
						check = x1*x1 + y1*y1 +z*z;
						System.out.println("checkxyz="+check);
					}
					
					z1 = (-b -Math.sqrt(delta))/2*a;
					a = m*m+n*n;
					b = 2*n*p*z1;
					c = z1*z1*p*p-(1*xishu-z1*z1)*m*m;
					delta = b*b -4*a*c;
					if(a != 0 && delta >=0){
						y = (-b +Math.sqrt(delta))/2*a;
						x = Math.sqrt(1*xishu-y*y-z1*z1);
						re[4] = new double[]{x,y,z1};
						check = x*x + y*y +z1*z1;
						System.out.println("checkxyz="+check);
						
						
						x1 = -Math.sqrt(1*xishu-y*y-z1*z1);
						check = x1*x1 + y*y +z1*z1;
						System.out.println("checkxyz="+check);
						re[5] = new double[]{x1,y,z1};
						
						y1 = (-b -Math.sqrt(delta))/2*a;
						x = Math.sqrt(1*xishu-y1*y1-z1*z1);
						re[6] = new double[]{x,y1,z1};
						check = x*x + y1*y1 +z1*z1;
						
						System.out.println("checkxyz="+check);
						
						x1 = -Math.sqrt(1*xishu-y1*y1-z1*z1);
						re[7] = new double[]{x1,y1,z1};
						check = x1*x1 + y1*y1 +z1*z1;
						System.out.println("checkxyz="+check);
						
					}
					
					
					double y3 = (m*d-(A*p-C*m)*z)/(A*n-B*m);
					double x3 = (-p*z-n*y3)/m;
					
					System.out.println("source vector ,xyz="+x3+","+y3+","+z);
				//}
				
			//	else if(Direction <0){
					
					
					
					
				
				
				
				
			}else{
				System.out.println("no answer");
			}
		}else if(fenmu !=0){
			z = -c/b;
			x = -z*p -(n*m*d-n*A*p*z+n*C*m*z)/(A*n-B*m);
			y = (m*d-(A*p-C*m)*z)/(A*n-B*m);
			re[0] = new double[]{x,y,z};
		}
		
		if(A==0 &&B==0&& z ==0 ){

			if ( Direction == 1){
				y = m/Math.sqrt(m*m+n*n);
				x = n/Math.sqrt(m*m+n*n) ;
			}else if(Direction == -1){
				y = - m/Math.sqrt(m*m+n*n);
				x = -n /Math.sqrt(m*m+n*n); 
			}
			re[0] = new double[]{x,y,z};
		}
		
		if(A== 0 &&C ==0 && y==0){
			
			if(Direction == 1){
				z = m / Math.sqrt(m*m+p*p);
				x = p/Math.sqrt(m*m+p*p); 
			}else {
				z = -m / Math.sqrt(m*m+p*p);
				x =- p/Math.sqrt(m*m+p*p); 
			}
			re[0] = new double[]{x,y,z};
		}
		
		if(B==0 &&C ==0 && x ==0 ){
			if(Direction == 1){
				y = p / Math.sqrt(n*n+p*p);
				x = n /Math.sqrt(n*n+p*p); 
			}else {
				y = -p / Math.sqrt(m*m+p*p);
				x =- n/ Math.sqrt(m*m+p*p); 
			}
			re[0] = new double[]{x,y,z};
		}
		
		
		
		return re;
	}
	
	
	public static double [] get_vertical_biVector1(double m,double n,double p,double A,double B,double C, double d,double x0,double y0,double z0,double Direction){
		double []re = null;
		double k = d*(A*n-B*m) +B*(A*(m*x0+n*y0+p*z0)+m*d)+A*(A*n-B*m)*x0;
		double q = A*(m*x0+n*y0+p*z0)+m*d-(A*n-B*m)*y0;
		double t = (A*n-m*B)*(A*n-B*m)*A*A;
		double a = (B*A*p-A*n*C)*(B*A*p-A*n*C)+A*A*(A*p-C*m)*(A*p-C*m)+t;
		double b= -2*(B*A*p-A*n*C)*k-2*q*A*A*(A*p-C*m)-2*t*z0 ;
		double c = k*k+A*A*q*q+t*z0*z0-A*A*(A*n-B*m)*(A*n-B*m);
		
		

		double delta = b*b - 4*a*c;
		double fenmu = A*n - B*m ;
		double x = 0 ;
		double y = 0 ;
		double z = 0 ;
		System.out.println("delta="+delta);

		if(a !=0  && fenmu !=0){
			
			if(delta >= 0 ){
				if(Direction >0){
					z = (-b +Math.sqrt(delta))/2*a;
					
				}else {
					z = (-b -Math.sqrt(delta))/2*a;
				}
				y = (A*(m*x0+n*y0+p*z0)+m*d-(A*p-m*C)*z)/(A*n-B*m);
				x = (m*x0+n*y0+p*z0-n*y-p*z)/m;
				

			}else{
				System.out.println("no answer");
			}
			re = new double[]{x-x0,y-y0,z-z0};
		}else if(fenmu !=0){
			z = -c/b;
			y = (A*(m*x0+n*y0+p*z0)+m*d-(A*p-m*C)*z)/(A*n-B*m);
			x = (m*x0+n*y0+p*z0-n*y-p*z)/m;
			re = new double[]{x-x0,y-y0,z-z0};
		}
		
		if(A==0 &&B==0&& z ==0 ){
			a = m*m + n *n ;
			b = -2*p*z0*n - 2*n*n*y0-2*y0*m*m;
			c = p*p*z0*z0 + n*n*y0*y0 + m*m*y0*y0 - m*m*(1-z0*z0);  
			if ( Direction > 0){
				y = (-b +Math.sqrt(delta))/2*a;
				x = (p*z0 - n*(y-y0))/m+x0 ;
			}else if(Direction <0){
				y = (-b -Math.sqrt(delta))/2*a;
				x = (p*z0 - n*(y-y0))/m+x0 ;
			}
			re = new double[]{x-x0,y-y0,z-z0};
		}
		
		if(A== 0 &&C ==0 && y==0){
			a = p*p + m*m ;
			b = -2*p*n*y0;
			c = n*n*y0*y0 -m*m +m*m*y0*y0;
			if(Direction>0){
				z = (-b +Math.sqrt(delta))/2*a + z0;
				x = (n*y0 - p*(z-z0))/m + x0; 
			}else {
				z = (-b -Math.sqrt(delta))/2*a + z0;
				x = (n*y0 - p*(z-z0))/m + x0; 
			}
			re = new double[]{x-x0,y-y0,z-z0};
		}
		
		if(B==0 &&C ==0 && x ==0 ){
			a = p*p + n*n ;
			b = -2*m*n*x0;
			c = m*m*x0*x0 -p*p +p*p*x0*x0;
			if(Direction>0){
				y = (-b +Math.sqrt(delta))/2*a + y0;
				z = (m*x0 - p*(z-z0))/n + y0; 
			}else {
				y = (-b -Math.sqrt(delta))/2*a + y0;
				z = (m*x0 - p*(z-z0))/n + y0; 
			}
			re = new double[]{x-x0,y-y0,z-z0};
		}
		
	
		
		return re;
	}
	
	
	
	/***********************************
	 * 求点 x,y,z
	 * @param startx
	 * @param starty
	 * @param startz
	 * @param endx
	 * @param endy
	 * @param endz
	 * @param R
	 * @return
	 */
	public static double getPx(double m,double n,double p,double startx,double R,double Direction){
	double x;
	double L= m*m +n*n+p*p;
	System.out.println("L="+L);
	double t = 0 ;
	
	if(Direction >0){
		t = R/Math.sqrt(L);
	}else if(Direction <0){
		t = -R/Math.sqrt(L);
	}
	x = m*t +startx;
	
	return x;
	
	}

	public static double getPy(double m,double n,double p,double starty,double R,double Direction){
	
	double y;
	double L= m*m +n*n+p*p;
	double t = 0 ;
	
	if(Direction > 0){
		t = R/Math.sqrt(L);
	}else if(Direction <0){
		t = -R/Math.sqrt(L);
	}
	
	y = n*starty + t;
	return y;
	}

 /********************************
  * Direction == 1,为top
  * Direction == -1, 为bottom
  * @param m
  * @param n
  * @param p
  * @param startz
  * @param R
  * @param Direction
  * @return
  */
	public static double getPz(double m,double n,double p,double startz,double R,double Direction){
		double z;
		double L= m*m +n*n+p*p;
		double t = 0 ;
		
		if(Direction >0){
			t = R/Math.sqrt(L);
		}else if(Direction <0){
			t = -R/Math.sqrt(L);
		}
		
		z = p*t + startz;
		
		return z;
	}

	
	
	
	
	/******************************************************
	 * 
	 */
	public static void generateCoverage(){
		int binsize = 40000 ; //40kb
		int start =1;
		int enzymeseed = 500;
		float gcf = 1.0f;
		float mapscoref = 1.0f;
		int chrom = 1 ;
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\test.cov"));
			for(int i=0;i<6182;i++){
				start = i*binsize ;
				int end = (i+1) *binsize;
				double d = Math.random();
				String ds = new java.text.DecimalFormat("#.0000").format(d);
				int feq = (int)(d*500);
				double t = Math.random();
				String ts = new java.text.DecimalFormat("#.0000").format(t);
				bw.write(chrom+"\t"+start+"\t"+end+"\t"+feq+"\t0"+ds+"\t0"+ts+"\n");
			}
			bw.close();
		}catch(Exception ex){
			ex.printStackTrace() ;
		}
		
		
	}
	
	
	/*******************************************************
	 * 
	 * @return
	 */
	public static double [] get_vertical_biVector2(double a1,double a2,double a3,double b1,double b2,double b3,double c1,double c2,double c3){
		
		double []re = null ;
		
		double x=0;
		double y=0;
		double z=0;
		
		

		double m = Math.sqrt((a1-b1)*(a1-b1)+(a2-b2)*(a2-b2)+(a3-b3)*(a3-b3)) ;
		double m1= Math.sqrt((c1-b1)*(c1-b1)+(c2-b2)*(c2-b2)+(c3-b3)*(c3-b3));
		
		double mu =  (m*(c1-b1)+m1*(a1-b1) )/(m*m1) ;
		double nv = (m*(c2-b2)+m1*(a2-b2))/(m*m1);
		double pw= (m*(c3-b3)+m1*(c3-b3))/(m*m1) ;
		
		double k1 = (a3-b3)/ m ;
		double k2 = (c3-b3)/ m1;
		double k3 = (a1-b1)/m;
		double k4 = (c1-b1)/m1;
		
		double k5 = (a2-b2)/m;
		double k6 = (c2-b2)/m1;
		
		
		if(k1==0 && k2 ==0){ //z=0
			
			re = new double[]{0,0,1};
		}else if(k3==0 && k4==0){ //x=0;
			x = 0 ;
			y = pw / Math.sqrt(nv*nv+pw*pw);
			z = nv /Math.sqrt(nv*nv+pw*pw); 
			re = new double[]{1,0,0};
		}else if(k5==0&&k6==0){ // y=0
			y =0;
			z = mu / Math.sqrt(mu*mu+pw*pw);
			x = pw / Math.sqrt(mu*mu+pw*pw);
			re = new double[]{0,1,0};
		}else {
			double x_fenzi=((-a3*b1+c3*b1+a1*b3+a3*c1-b3*c1-a1*c3)*((a3*b2-c3*b2-a2*b3-a3*c2+b3*c2+a2*c3)*(k1+k2)-(a2*b1-c2*b1-a1*b2-a2*c1+b2*c1+a1*c2)*(k3+k4)));
			System.out.println("xfenzi="+x_fenzi);
			double x_fenmu = ((a3*b2-c3*b2-a2*b3-a3*c2+b3*c2+a2*c3)*((a3*b2-c3*b2-a2*b3-a3*c2+b3*c2+a2*c3)*(k5+k6)-(-a3*b1+c3*b1+a1*b3+a3*c1-b3*c1-a1*c3)*(k3+k4)));
			System.out.println("x_fenmu="+x_fenmu);
			
			x = x_fenzi/x_fenmu - (a2*b1-c2*b1-a1*b2-a2*c1+b2*c1+a1*c2)/(a3*b2-c3*b2-a2*b3-a3*c2+b3*c2+a2*c3) ;
			
			
			double x2_fenzi = -((a3*b2-c3*b2-a2*b3-a3*c2+b3*c2+a2*c3)*(k1+k2)-(a2*b1-c2*b1-a1*b2-a2*c1+b2*c1+a1*c2)*(k3+k4));
			System.out.println("x2_fenzi="+x2_fenzi);
			
			double x2_fenmu = ((a3*b2-c3*b2-a2*b3-a3*c2+b3*c2+a2*c3)*(k5+k6)-(-a3*b1+c3*b1+a1*b3+a3*c1-b3*c1-a1*c3)*(k3+k4));
			System.out.println("x2_fenmu="+x2_fenmu);
			 y =  x2_fenzi/x2_fenmu;
			
			re = new double[]{x,y,1};
		}
		
		
		
		
		
		
		
		return re;
		
	}
	
	
	
	
	public static double CalulateXYAnagle(float startx,float starty,float endx,float endy){
		
		
		
		double tan = Math.atan(Math.abs((endy - starty)/(endx - startx))) * 180/Math.PI;
		//double tan = Math.atan((endy - starty)/(endx - startx));
		if (endx > startx && endy > starty)//one
		{
			return -tan;
			}
			else if (endx > startx && endy < starty)//second
	          {
	              return tan;
	          }
	          else if (endx < startx && endy > starty)//third
	          {
	              return tan - 180;
	          }
	          else
	          {
	              return 180 - tan;
	          }
	
//	return tan;
	}
	
	
	
	
	public static void generateHic70(){
		String [] arrs = new String[]{"xaa","xab","xac","xad","xae","xaf","xag","xah","xai","xaj","xak","xal","xam","xan"};
		for(String str:arrs){
			
			StringBuffer sb = new StringBuffer();
			sb.append("java -Xmx8000m -Xms8000m  -jar /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/K562FeatureStart.jar");
			sb.append(" /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/split_hic070/"+str);
			sb.append(" /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/physical/hic070_1_"+str+".feature");
			sb.append(" /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/idlist/split_hic70_694_1/"+str);
			sb.append(" /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/idlist/split_hic70_694_2/"+str);
			
			System.out.println(sb.toString());
			
		}
	
	}
	
	
	
	public static void generateHic69(){
		String [] arrs = new String[]{"xaa","xab","xac","xad","xae","xaf","xag","xah","xai","xaj","xak"};
		for(String str:arrs){
			
			StringBuffer sb = new StringBuffer();
			sb.append("java -Xmx8000m -Xms8000m  -jar /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/K562FeatureStart.jar");
			sb.append(" /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/split_hic069/"+str);
			sb.append(" /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/physical/hic069_1_"+str+".feature");
			sb.append(" /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/idlist/split_hic069_693_1/"+str);
			sb.append(" /leofs/bioweb/tangbx/visualization/download_data/GSE63525_3dmap/K562/idlist/split_hic069_693_2/"+str);
			
			System.out.println(sb.toString());
			
		}
	
	}	
	
	
	
	
	
	
	
}
