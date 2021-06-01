package main.firefighters;

import main.api.CityNode;
import main.api.Firefighter;

public class FirefighterImpl implements Firefighter {

	private CityNode cityNode;
	private int distanceTraveled = 0;

	public FirefighterImpl(CityNode cityNode, int distanceTraveled) {
		this.cityNode = cityNode;
		this.distanceTraveled = distanceTraveled;
	}

//	@Override
	public void setLocation(CityNode cityNode) {
		this.cityNode = cityNode;
	}

	@Override
	public CityNode getLocation() {
		return this.cityNode;
	}

	@Override
	public int distanceTraveled() {
		return this.distanceTraveled;
	}

	public void setDistanceTraveled(int distance) {
		this.distanceTraveled = distance;
	}
}
