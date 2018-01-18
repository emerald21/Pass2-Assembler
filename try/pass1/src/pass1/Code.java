package pass1;
import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.util.*;

class Mnemonic{
	
	String mname,opcode,length,stmnt;
	
}

class data{
	int seq;
	String value;
	int addr;
	
	data(){
		addr=0;
		
	}
}

public class Code{
	public static void main(String[] args)throws Exception
	{
		ArrayList<data>sym=new ArrayList<data>();
		ArrayList<data>lit=new ArrayList<data>();
		ArrayList tokens=new ArrayList();
		
		File file = new File("/home/aditi/try/inputcode.asm");

		BufferedReader br = new BufferedReader(new FileReader(file));
 
		String st;
		while ((st = br.readLine()) != null)
		{
			StringTokenizer str=new StringTokenizer(st," ,");
			while (str.hasMoreTokens()) 
			{
				tokens.add(str.nextToken());
				//	  System.out.println(str.nextToken());
				
			}
			
		}
		
		/*
		 * Displaying tokenized input file 
        */
        //System.out.println(tokens.toString());
  
    	
  	 
		br.close();
		File file1 = new File("/home/aditi/try/Mnemonic.txt");
	
		BufferedReader br1 = new BufferedReader(new FileReader(file1));
   
		ArrayList<Mnemonic> m=new ArrayList<Mnemonic>();
   
		String st1;
		while ((st = br1.readLine()) != null)
		{
			StringTokenizer str=new StringTokenizer(st," ");
			while (str.hasMoreTokens())
			{
				Mnemonic obj=new Mnemonic();
				obj.mname=str.nextToken();
				obj.stmnt=str.nextToken();
				obj.opcode=str.nextToken();
				obj.length=str.nextToken();
				
				m.add(obj);
				
				//System.out.println(str.nextToken());
			}
			
		}
		br1.close();
		
		/*
		 * Displaying tokenized mnemonics
		 * 
		 *     for(int i=0;i<m.size();i++)
		 *         {
		 *             	System.out.println(m.get(i).mname);
		 *                 	System.out.println(m.get(i).stmnt);
		 *                     	System.out.println(m.get(i).opcode);
		 *                         	System.out.println(m.get(i).length);
		 *                             }
		 *                             
		 */
		
		//--------------------------------------------File DEcleration-----------------------------------------------
		
		File lit1=new File("/home/aditi/try/lit.txt");
		
		if(lit1.exists()){
			lit1.delete();
		}
		File litfile=new File("/home/aditi/try/lit.txt");
		int cntlit=0;
		File ic1=new File("/home/aditi/try/ic.txt");
		if(ic1.exists()){
			ic1.delete();
		}
		File ic=new File("/home/aditi/try/ic.txt");
		FileWriter fr1=new FileWriter(ic);
		int lc=0;
		File sym1=new File("/home/aditi/try/sym.txt");
		if(sym1.exists()){
			sym1.delete();
		}
		File symfile=new File("/home/aditi/try/sym.txt");
		File poolt=new File("/home/aditi/try/pool.txt");
		if(poolt.exists()){
			poolt.delete();
		}
		File pool=new File("/home/aditi/try/pool.txt");
		FileWriter fr2=new FileWriter(pool);
		int cntsym=0;
		int check=0;
		
		//-----------------------------------------------loop--------------------------------------------------
		
		for(int i=0;i<tokens.size();i++)
		{
			String str=(String)tokens.get(i);
			//System.out.println(str);
			
			if(str.equals("START")){
				lc=Integer.parseInt(tokens.get(i+1).toString());
				
				i++;
			
			}
		
			
			else if(str.contains("=")){
				
				 data a=new data();
		
				cntlit++;
				 a.seq=cntlit;
				 
				 a.value=new String(str.substring(2,3));
				 lit.add(a);
				fr1.write(" (L, "+cntlit+")");
				
				}
				
			else if((str.equals("AREG"))||(str.equals("BREG"))||(str.equals("CREG"))||(str.equals("DREG")))
					{
				fr1.write(" "+str);
			}
			 
			else if((str.equals("MOVER"))||(str.equals("MOVEM"))||(str.equals("ADD"))||(str.equals("SUB"))||(str.equals("MULT"))||(str.equals("STOP"))||(str.equals("BC"))||(str.equals("COMP"))||(str.equals("DIV"))||(str.equals("READ"))||(str.equals("PRINT")))
			{
				
				int g=0;
				for(int j=0;j<m.size();j++)
				{
					if(m.get(j).mname.equals(str))
					{
						fr1.write("\n"+lc+" "+"("+m.get(j).stmnt+","+m.get(j).opcode+")"+",");
						g=Integer.parseInt(m.get(j).length);
						break;
					}
				}
				
			lc=lc+g;
			}
			  
			else if( (str.equals("ORIGIN")) ||(str.equals("EQU"))||(str.equals("END"))||(str.equals("LTORG")))
			{
				
				if(str.equals("END")){
					fr1.write("\n"+"    "+"(AD,02)");
					int x=0;
					for(int y=0;y<lit.size();y++)
					{
						if( ( lit.get(y).addr==0)){
							lit.get(y).addr=lc;
							
							if(x==0)
								x=lit.get(y).seq;
							lc++;
							i++;
						}
					}
				fr2.write("#"+x+"\n");
					break;
				}
				else if(str.equals("ORIGIN"))
				{
			
					fr1.write("\n"+"    "+"(AD,03) "+tokens.get(i+1).toString());
					
					//str contains origin, token(i+1) contains loop+2
					
					String nxtstr=tokens.get(i+1).toString();
					StringTokenizer st3=new StringTokenizer(nxtstr,"+",false);
					String loop=st3.nextToken();
					 int a=Integer.parseInt(st3.nextToken().toString());
				
					int p=0;
					 for(int y=0;y<sym.size();y++)
					{
						if(sym.get(y).value.equals(loop))
						{
				
						p=sym.get(y).addr;
							break;
						}
					}
				
					lc=p+a;
					i++;
					 
				
				}
				else if(str.equals("EQU"))
				{
					fr1.write("\n"+"    "+"(AD,04) "+tokens.get(i+1).toString());
					String prev=tokens.get(i-2).toString();
					String next=tokens.get(i+1).toString();
					int local=0;
					for(int u=0;u<sym.size();u++)
					{
						if(sym.get(u).value.equals(next))
						{
							local=sym.get(u).addr;
							break;
						}
					}
					for(int u=0;u<sym.size();u++)
					{
						if(sym.get(u).value.equals(prev))
						{
							sym.get(u).addr=local;
							break;
						}
					}
					
				i++;
				}
				else if(str.equals("LTORG")){
					
					fr1.write("\n");
					i++;
				int x=0;
		     		for(int y=0;y<lit.size();y++)
					{
						if( ( lit.get(y).value.equals(tokens.get(i).toString().substring(2,3)) ) && (lit.get(y).addr==0) ){
							lit.get(y).addr=lc;
							if(x==0)
							x=lit.get(y).seq;
							
							lc++;
							i++;
						}
					}	
		     		fr2.write("#"+x+"\n");
					i--;
				}
		
				
			}
			else if(str.equals("DS")){
				
				fr1.write("\n"+lc+" (DL,02) "+tokens.get(i+1).toString());
				int p=Integer.parseInt(tokens.get(i+1).toString());
				lc=lc+p;
				i++;
			}
			 else
		      {
				 int l=0;
				if(check==0)
				 {
					
					 data b=new data();
					 cntsym++;
					 b.seq=cntsym;
					 b.value=new String(str);
					 if(tokens.get(i+1).equals(":"))
					 {
						 b.addr=lc;
						 i++;
						l=1;
						
					 }
					 sym.add(b);
					 if(l==0)
						 fr1.write(" ("+b.value.toString()+", "+b.seq+")");
					 check=1;
				 }
				 else{
					 int f=0;
					int r=0,k=0;
				 for(k=0;k<sym.size();k++)
				 {
					 if(sym.get(k).value.equals(str))
					 {
						 if(tokens.get(i+1).equals(":"))
						 {
							 sym.get(k).addr=lc;
							i++;
						r=1;
						 }
						 
						f=1;
						break;
					 }
				 }
				 if(f==1)
				 {
					 if(r==0)
						 fr1.write(" ("+str+", "+sym.get(k).seq+")");
				 }
				 
				 else{
					 int p=0;
				 data b=new data();
				 cntsym++;
				 b.seq=cntsym;
				 b.value=new String(str);
				 if(tokens.get(i+1).equals(":"))
				 {
					 b.addr=lc;
					 i++;
					 p=1;
				 }
				 sym.add(b);
				 if(p==0)
				 fr1.write(" ("+b.value.toString()+", "+b.seq+")");
				 }
				
		      }
		      }
		
		}

		
		FileWriter fw= new FileWriter(litfile);
		for(int y=0;y<lit.size();y++)
		{
			fw.write(lit.get(y).seq+" "+lit.get(y).value+" "+lit.get(y).addr+"\n");
		}
		
		fw.close();
		
		FileWriter fw1= new FileWriter(symfile);
		for(int y=0;y<sym.size();y++)
		{
			fw1.write(sym.get(y).seq+" "+sym.get(y).value+" "+sym.get(y).addr+"\n");
		}
		
		fw1.close();
		fr1.close();
		fr2.close();
		
		}
		
		
	}