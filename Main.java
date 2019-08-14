/**
 * Challenge 3
 * Author: Bingya Liu
 * Date: 06/15/2019
 **/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.util.*;
import java.lang.Object;

public class Main {
    

	public static class Asset {
		/** the Asset class **/
		String name;
		String type;
		int shares;
		int price;
		double accruedInterest;
        
		
		public Asset( String name, String type, int shares, int price, double accruedInterest){
			/** constructor that takes 5 parameters, given in the challenge**/
			this.name = name;
			this.type = type;
			this.shares = shares;
			this.price = price;
			this.accruedInterest = accruedInterest;
		}
        
		
		public double getMarketValue(){
			/** method that gets the market value of the asset based on type **/
			if (this.type.equals("STOCK")) {
				return this.shares*this.price;
			}
			else {
				return this.shares*(this.price+this.accruedInterest)*0.01;
			}
		}
        
		
		//Getters
		public String getName(){
			return this.name;
		}
      
        public String getType(){
            return this.type;
        }
		
		public double getPrice(){
			return this.price;
		}
      
        public double getAccruedInterest(){
            return this.accruedInterest;
        }
      
        
	}// End of Asset class
    
    
  
	public static class CollectionOfAssets {
		/** the Collection of Assets class **/
		ArrayList<Asset> assets;
		
		
		public CollectionOfAssets(String[] items){
			/** constructor **/
			this.assets = new ArrayList<Asset>();
			ManageAssets(items);
		}
        
		
		private void ManageAssets(String[] items){
			/** method that create new Asset objects using input string[] **/
			for(int i=0; i<items.length; i++){
				String[] item = items[i].split(",");
				String name = item[0];
				String type = item[1];
				int shares = Integer.parseInt(item[2]);
				int price = Integer.parseInt(item[3]);
				Double accruedInterest = Double.parseDouble(item[4]);
				Asset newAsset = new Asset( name, type, shares, price, accruedInterest);
				assets.add(newAsset);
			}
		}
        
		
		public ArrayList<Asset> getAssets(){
			/** method that returns an ArrayList of assets **/
			return this.assets;
		}
        
		
		public double TotalMarketValue(){
			/** method that returns total market value of the collection of assets **/
			double sum = 0.0;
			for(int i=0; i<this.assets.size(); i++){
				sum = sum + assets.get(i).getMarketValue();
			}
			return sum;
		}
      
	}// End of CollectionOfAssets class
    
    
	
	public static class Trade {
		/** the Trade class **/
		HashMap<String,Double> map;
		double benchmarkTMV, portfolioTMV;
        
		
		public Trade( CollectionOfAssets portfolio, CollectionOfAssets benchmark){
			/** constructor that takes two CollectionOfAssets Objects, one portfolio and one benchmark **/
			this.map = new HashMap<String,Double>();
			this.benchmarkTMV = benchmark.TotalMarketValue();
			this.portfolioTMV = portfolio.TotalMarketValue();
            
			//map stores <Name of Assets, benchmark Market percentage> key-value pairs
			for(Asset asset: benchmark.getAssets()){
				this.map.put(asset.getName(), asset.getMarketValue()/benchmarkTMV);
			}
          
            
			for(Asset asset: portfolio.getAssets()){
				//map stores <Name of Assets, differenence in benchmark&portfolio Market percentage> key-value pairs
				if (this.map.containsKey(asset.getName())) {
					this.map.put(asset.getName(), map.get(asset.getName())-asset.getMarketValue()/portfolioTMV);
                  
					//map stores <Name of Assets, number of shares> key-value pairs based on the types of assets
					if (asset.getType().equals("STOCK")) {
						this.map.put(asset.getName(), map.get(asset.getName())/asset.getPrice());
					}
					else {
						this.map.put(asset.getName(), map.get(asset.getName())/(asset.getPrice()+asset.getAccruedInterest())*100.0);
					}
					this.map.put(asset.getName(), map.get(asset.getName())*this.portfolioTMV);
				}
			}
		}
        
		
		public void Results(){
			/** method that handles the correct printout format based on postive/neagtive **/
			Collection<String> keys = this.map.keySet();
			String[] keySet = keys.toArray(new String[keys.size()]);
			Arrays.sort(keySet); //alphabetic order
          
    			for(String key: keySet) {
    				if(map.get(key)>0){
					System.out.println("BUY"+","+key+","+Math.round(map.get(key)));
    				}
    				else if(map.get(key)<0) {
					System.out.println("SELL"+","+key+","+Math.round(map.get(key)*-1));
    				}
    				else {
				}
			}
		}
    
	}// End of Trade class
    
	
	public static void main(String[] args) throws IOException {
		/** the main method **/
		InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
		BufferedReader in = new BufferedReader(reader);
		String line;
		
		while ((line = in.readLine()) != null) {
			
			String[] string = line.split(":");
			String[] portfolioItems = string[0].split("\\|");
			String[] benchmarkItems = string[1].split("\\|");
			CollectionOfAssets portfolio = new CollectionOfAssets(portfolioItems);
			CollectionOfAssets benchmark = new CollectionOfAssets(benchmarkItems);
			Trade trade = new Trade(portfolio, benchmark);
			trade.Results();
		}
	}
}
