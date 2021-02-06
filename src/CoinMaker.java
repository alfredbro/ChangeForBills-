import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


@FunctionalInterface
interface CoinVerifyInterface {

    Float verifyCoins();
}

@FunctionalInterface
interface CoinMakerInterface {

    void convertToCoins(Float billAmount);
}


public class CoinMaker {

	private Map<Float, Integer> coinsMap = null;
	
	CoinMaker() {
		this.coinsMap = new LinkedHashMap<Float, Integer>(); 
	}
	
	public static void main(String[] args) {

		CoinMaker coinMaker = new CoinMaker();
		coinMaker.configureCoins();


	    System.out.println("lambda");
		System.out.println("AFTER Config valueAllCoins = " + 
		coinMaker.ref.verifyCoins());
		
		
		Float billAmount = 48.0f;
		coinMaker.ref2.convertToCoins(billAmount);

		// 2nd run
		billAmount = 3.0f;
		coinMaker.ref2.convertToCoins(billAmount);
	}
	
	private void configureCoins() {

		// TODO: handle float type better (don't use 'f')

		this.coinsMap.put(0.25f, 125);
		this.coinsMap.put(0.10f, 110);
		this.coinsMap.put(0.05f, 105);
		this.coinsMap.put(0.01f, 101);		
	}
	
	private CoinVerifyInterface ref = () -> {

 		Float valueAllCoins = 0.0f;
 		for(Entry<Float, Integer> coinEntry : this.coinsMap.entrySet()) {
 			
 			System.out.println("there are [" + 
 			coinEntry.getValue() +
 			"] coins of size [" + 
 			coinEntry.getKey() + "]");
 			
 			valueAllCoins += coinEntry.getKey() * coinEntry.getValue();
 		}

 		return valueAllCoins;
    };
     
 	private CoinMakerInterface ref2 = (billAmount) -> {
 		
		System.out.println("");
		System.out.println("");
		System.out.println("billAmount = " + billAmount);
		System.out.println("");
		
		for(Entry<Float, Integer> coinEntry : coinsMap.entrySet()) {
			
			Float coinValue = coinEntry.getKey() *
							coinEntry.getValue();
			
			if(billAmount >= coinValue) {
			
				billAmount -= coinValue;
				
				System.out.println("Used All [" + 
				coinEntry.getValue() + 
				"] coins, with total value [" +
				coinValue +
				"]");
				
				coinEntry.setValue(0);
				
			} else {
				Integer coinsUsed = 
					(int) (billAmount / coinEntry.getKey());
				
				billAmount -= (int) (coinsUsed * coinEntry.getKey());
				
				System.out.println("Used [" +
				coinsUsed + 
				"] coins of denomination [" + 
				coinEntry.getKey() + 
				"], with total value [" +
				coinsUsed * coinEntry.getKey() +
				"]");
				
				coinEntry.setValue((int) (coinEntry.getValue() - coinsUsed));
			}

			System.out.println("AFTER LOOP ITERATION, there are [" + 
			coinEntry.getValue() +
			"] coins of size [" + 
			coinEntry.getKey() + "]");
		}

		Integer totalCoinsLeft = 0;
		for(Entry<Float, Integer> coinEntry : coinsMap.entrySet()) {
			totalCoinsLeft += coinEntry.getValue();
		}

		if(totalCoinsLeft == 0) {
			System.out.println("");
			System.out.println("Machine is out of Coins");
			System.out.println("");
			System.out.println("");
		}
 	};
}
