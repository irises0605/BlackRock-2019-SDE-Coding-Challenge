import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.util.*;
import java.lang.Object;

public class Main {

	public static class Asset {
		String name;
		String type;
		int shares;
		int price;
		double accruedInterest;
		
		public Asset( String name, String type, int shares, int price, double accruedInterest){
			this.name = name;
			this.type = type;
			this.shares = shares;
			this.price = price;
			this.accruedInterest = accruedInterest;
		}
		
		public double getMarketValue(){
			if (this.type.equals("STOCK")) {
				return this.shares*this.price;
			}
			else {
				//System.out.println(this.shares*(this.price+this.accruedInterest)*0.01);
				return this.shares*(this.price+this.accruedInterest)*0.01;
			}
		}
		
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
        
	}
  
	public static class CollectionOfAssets {
		ArrayList<Asset> assets;
		//HashMap<String,Double> pairs;
		
		
		public CollectionOfAssets(String[] items){
			this.assets = new ArrayList<Asset>();
			//this.pairs = new HashMap<String,Double>();
			ManageAssets(items);
		}
		
		private void ManageAssets(String[] items){
            
			for(int i=0; i<items.length; i++){
                String[] item = items[i].split(",");
				String name = item[0];
				String type = item[1];
				int shares = Integer.parseInt(item[2]);
				int price = Integer.parseInt(item[3]);
				Double accruedInterest = Double.parseDouble(item[4]);
				Asset newAsset = new Asset( name, type, shares, price, accruedInterest);
				assets.add(newAsset);
				//pairs.put(name, price);
			}
		}
		
		public ArrayList<Asset> getAssets(){
			return this.assets;
		}
// 		
// 		public double getPrice( String name){
// 			return this.pairs.get(name);
// 		}
		
		public double TotalMarketValue(){
			double sum = 0.0;
			for(int i=0; i<this.assets.size(); i++){
				sum = sum + assets.get(i).getMarketValue();
			}
			return sum;
		}

	}
	
	public static class Trade {
	
		HashMap<String,Double> map;
		double benchmarkTMV, portfolioTMV;
		
		public Trade( CollectionOfAssets portfolio, CollectionOfAssets benchmark){
			this.map = new HashMap<String,Double>();
			this.benchmarkTMV = benchmark.TotalMarketValue();
			this.portfolioTMV = portfolio.TotalMarketValue();
			for(Asset asset: benchmark.getAssets()){
				this.map.put(asset.getName(), asset.getMarketValue()/benchmarkTMV);
			}
			for(Asset asset: portfolio.getAssets()){
				if (this.map.containsKey(asset.getName())) {
                  this.map.put(asset.getName(), map.get(asset.getName())-asset.getMarketValue()/portfolioTMV);
                  if (asset.getType().equals("STOCK")){
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
		
			Collection<String> keys = this.map.keySet();
    		String[] keySet = keys.toArray(new String[keys.size()]);
    		Arrays.sort(keySet);
    		for(String key: keySet){
    			if(map.get(key)>0){
    			System.out.println("BUY"+","+key+","+Math.round(map.get(key)));
    			}
    			else if(map.get(key)<0){
    			System.out.println("SELL"+","+key+","+Math.round(map.get(key)*-1));
    			}
    			else{
    			}
    		}
		}
	}
	
	public static void main(String[] args) throws IOException {
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