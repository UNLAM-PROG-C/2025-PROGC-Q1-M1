package Ejercicio3;

import java.util.concurrent.*;

public class Main
{
	private static final int TIME = 100;
	private static final int NUM_ASSISTANTS = 2;
	
	private static final int PARKING_SIZE = 6;
	private static final int PITS_SIZE = 3;
	private static final int SERVICE_SIZE = 2;
	
	
	private static Semaphore parkingMTX = new Semaphore(PARKING_SIZE);
	private static Semaphore pitsMTX = new Semaphore(PITS_SIZE);
	private static Semaphore serviceMTX = new Semaphore(SERVICE_SIZE);
	
	
	private static BlockingQueue<String> parkedCars = new LinkedBlockingQueue<>();
	private static BlockingQueue<String> checkedCars = new LinkedBlockingQueue<>();
	private static BlockingQueue<String> pitsCars = new LinkedBlockingQueue<>();
	private static BlockingQueue<String> fixedCars = new LinkedBlockingQueue<>();
	
	private static int cars = 3;
	
	public static void main(String[] args)
	{

		Richard richard = new Richard();
		Aaron aaron = new Aaron();
		Assistant assistant1 = new Assistant("Rob");
		Assistant assistant2 = new Assistant("Bran");
		Charles charles = new Charles();
		
		richard.start();
		aaron.start();
		assistant1.start();
		assistant2.start();
		charles.start();

	}
	
	static class Richard extends Thread
	{

		public void run()
		{	
			try
			{
				while(thereCars())
				{

					parkingMTX.acquire();
					goParking();
					parkingMTX.release();
				}

				System.out.println("Richard avisa que no hay mas autos en la calle.");

				parkedCars.put("FIN");
			} 
			catch (InterruptedException e)
			{
				System.out.println("ERROR: Richard no pudo realizar su trabajo.");
			}

		}

		private boolean thereCars()
		{
			return cars > 0;
		}

		private void goParking() throws InterruptedException
		{
			System.out.println("Richard lleva un auto al estacionamiento.");

			Thread.sleep(TIME); //Simular demora

			parkedCars.put("AUTO");

			cars --;
		}

	}
	
	static class Aaron extends Thread
	{
		
		public void run()
		{	
			String nextCar;
			
			try
			{
				nextCar = parkedCars.take();
				
				while(!nextCar.equals("FIN"))
				{
					checkCar();
					
					nextCar = parkedCars.take();
				}
				
				System.out.println("Aaron avisa que no hay mas autos en el estacionamiento.");
				
				for (int i = 0; i < NUM_ASSISTANTS; i ++)
				{
				    checkedCars.put("FIN");
				}
			} 
			catch (InterruptedException e)
			{
				System.out.println("ERROR: Aaron no pudo realizar su trabajo.");	
			}
		}
		
		private void checkCar() throws InterruptedException
		{
			System.out.println("Aaron comienza la inspección del auto.");
			
			Thread.sleep(TIME); //Simular demora
			
			checkedCars.put("AUTO");
		}
		
	}
	
	static class Assistant extends Thread
	{
		String name = "";
		
		public Assistant(String name)
		{
			this.name = name;
		}
		
		public void run()
		{	
			try
			{	
				goPitBase();
				
				goServiceBase();
			} 
			catch (InterruptedException e)
			{
				System.out.println("ERROR: El asistente " + this.name + " no pudo realizar su trabajo.");	
			}
		}
		
		private void goPitBase() throws InterruptedException
		{
			String nextCar;
			
			nextCar = checkedCars.take();
			
			while(!nextCar.equals("FIN"))
			{
				pitsMTX.acquire();
				goPit();
				
				nextCar = checkedCars.take();
			}
			
			System.out.println("El asistente " + this.name + " avisa que no hay mas autos en el estacionamiento.");
			
			pitsCars.put("FIN");
		}
		
		private void goServiceBase() throws InterruptedException
		{
			String nextCar;
			
			nextCar = fixedCars.take();
			
			while(!nextCar.equals("FIN"))
			{
				serviceMTX.acquire();
				goService();
				serviceMTX.release();
				
				nextCar = fixedCars.take();
			}
			
			System.out.println("El asistente " + this.name + " avisa que no hay mas autos en las fosas.");
		}
		 
		private void goPit() throws InterruptedException
		{
			System.out.println("El asistente " + this.name + " lleva un auto a la fosa.");
			
			Thread.sleep(TIME); //Simular demora
			
			pitsCars.add("AUTO");
		}
		
		private void goService() throws InterruptedException
		{
			System.out.println("El asistente " + this.name + " lleva un auto al servicio.");
			
			Thread.sleep(TIME); //Simular demora
		}
		
	}
	
	static class Charles extends Thread
	{
		int numEnds = 0;
		
		public void run()
		{	
			String nextCar;
			
			try
			{
				
				while(this.numEnds < NUM_ASSISTANTS)
				{
					nextCar = pitsCars.take();
					
					if(nextCar.equals("FIN"))
					{
						this.numEnds ++;
					}
				
					else
					{
						fixCar();
					}
				}
				
				System.out.println("Charles avisa que no hay mas autos en las fosas.");
				
				for (int i = 0; i < NUM_ASSISTANTS; i ++)
				{
				    fixedCars.put("FIN");
				}
			} 
			catch (InterruptedException e)
			{
				System.out.println("ERROR: Charles no pudo realizar su trabajo.");	
			}
		}
		
		private void fixCar() throws InterruptedException
		{
			System.out.println("Charles comienza la reparación del auto.");
			
			Thread.sleep(TIME); //Simular demora
			
			pitsMTX.release();
			
			fixedCars.put("AUTO");
		}
		
	}

}
