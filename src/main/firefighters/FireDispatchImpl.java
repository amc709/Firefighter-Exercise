package main.firefighters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.api.Building;
import main.api.City;
import main.api.CityNode;
import main.api.FireDispatch;
import main.api.Firefighter;
import main.api.exceptions.NoFireFoundException;


public class FireDispatchImpl implements FireDispatch {

	private City city;
	private List<Firefighter> fireFighters;


	public FireDispatchImpl(City city) {
		this.city = city;
	}

	@Override
	public void setFirefighters(int numFirefighters) {
		if (numFirefighters > 0) {
			fireFighters = new ArrayList<>();
			CityNode node = this.city.getFireStation().getLocation();
			for (int i = 0; i < numFirefighters; i++) {
				fireFighters.add(new FirefighterImpl(node, 0));
			}
		}
	}

	@Override
	public List<Firefighter> getFirefighters() {
		return this.fireFighters;
	}

	@Override
	public void dispatchFirefighers(CityNode... burningBuildings) {

		// Starting position from fire station
		int stationX = this.city.getFireStation().getLocation().getX();
		int stationY = this.city.getFireStation().getLocation().getY();

		for (CityNode node : burningBuildings) {
			
			// How many firefighters are in the firestation?
			List<Firefighter> firefightersInStation = 
				this.fireFighters.stream()
					.filter( ff -> ff.getLocation().getX() == stationX 
						&& ff.getLocation().getY() == stationY)
					.collect(Collectors.toList());
			
			int nodeX = node.getX();
			int nodeY = node.getY();
			System.out.println("... Going to city node:  (" + nodeX + ", " + nodeY + ")");

			Building bldg = this.city.getBuilding(node);

			if (!bldg.isFireproof() && bldg.isBurning()) {

				for (Firefighter ff : this.fireFighters) {
					
					boolean isFirefighterInFireStation = 
							ff.getLocation().getX() == stationX
							&& ff.getLocation().getY() == stationY;	
					
					if (
						(
							// If he is not assigned to a location, send him.
							isFirefighterInFireStation
						)
						|| 
						(
							// If all other firefighters are out, this firefighter
							// can go to the location.
							!isFirefighterInFireStation
							&&	bldg.isBurning()
							&& firefightersInStation.size() == 0
						)
					) {
												
						int tmp = ff.distanceTraveled();
								
						// Measure distance from his starting point to the fire location.
						int ffX = ff.getLocation().getX();
						int ffY = ff.getLocation().getY();
						
						System.out.println("start at node:  (" + ffX + ", " + ffY + ")");
						
						if (Math.abs(nodeX - ffX) > 0) {
							tmp  += Math.abs(nodeX - ffX);
						} 

						if (Math.abs(nodeY - ffY) > 0) {
							tmp += Math.abs(nodeY - ffY);
						}
						((FirefighterImpl)ff).setDistanceTraveled(tmp);
	
						// Update location
						((FirefighterImpl)ff).setLocation(node);
						
						System.out.println("Total distance so far: " + ff.distanceTraveled());
						
						try {
							bldg.extinguishFire();
						} catch (NoFireFoundException e) {
							System.out.println("No fire here");
						}
						
						break;
					}

				}

			}

		}
	}
}
