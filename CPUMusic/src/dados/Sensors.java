package dados;
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.SigarException;


public class Sensors {

	private static Sigar sigar = new Sigar();

	public double getInformationsAboutMemory() {
		Sigar sigarmem = new Sigar();
		/*System.out.println("**************************************");
	        System.out.println("*** Informations about the Memory: ***");
	        System.out.println("**************************************\n");
		 */

		Mem mem = null;
		try {
			mem = sigarmem.getMem();
		} catch (SigarException se) {
			se.printStackTrace();
		}
		/*
	        System.out.println("Actual total free system memory: "
	                + mem.getActualFree() / 1024 / 1024+ " MB");
	        System.out.println("Actual total used system memory: "
	                + mem.getActualUsed() / 1024 / 1024 + " MB");
	        System.out.println("Total free system memory ......: " + mem.getFree()
	                / 1024 / 1024+ " MB");
	        System.out.println("System Random Access Memory....: " + mem.getRam()
	                + " MB");
	        System.out.println("Total system memory............: " + mem.getTotal()
	                / 1024 / 1024+ " MB");
	        System.out.println("Total used system memory.......: " + mem.getUsed()
	                / 1024 / 1024+ " MB");
		 */

		//System.out.println(mem.getUsedPercent());
		return mem.getUsedPercent();




	}

	public double getInformationsAboutCPU() {
		Sigar sigarcpu = new Sigar();
		double retorno = 0;
		/*System.out.println("**************************************");
	        System.out.println("*** Informations about the CPU: ***");
	        System.out.println("**************************************\n");
		 */

		try {
			//Se deixar o metodo getcpuperc ele divide o uso da CPU em varios tipos
			//como, usuario, sistema e tal, o que eu fiz foi pegar a que está em idle
			//e subtrair de 100% pra pegar todo o resto, conferir se isso ta certo depois.
			
			//System.out.println((1 - sigarcpu.getCpuPerc().getIdle())*100);
			retorno = ((1 - sigarcpu.getCpuPerc().getIdle())*100);
		} catch (SigarException e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	public String getInformationsAboutDISK() throws SigarException, InterruptedException {
        Sigar sigar = new Sigar();
        
        long prevTime = 0;
        long prevBytes = 0;
        //final long interval = 1 * 1000;

//        while (true) {
            long time = System.currentTimeMillis();
            long bytes = sigar.getDiskUsage("C:").getReadBytes();
            long rate = 0;
            if (prevTime != 0) {
                rate = (bytes - prevBytes) / ((time - prevTime));
                //System.out.println("irr" + (bytes - prevBytes) );
                //System.out.println("irr" + bytes );
                System.out.println("disk read bytes per second=" + Sigar.formatSize(rate));
            }
//            prevTime = time;
//            prevBytes = bytes;
//            Thread.sleep(1000);
//        }
            return Sigar.formatSize(rate);
	}




	}




