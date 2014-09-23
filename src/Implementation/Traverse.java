package Implementation;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Traverse {
	private ArrayList<Point> exhaustiveOrder = new ArrayList<Point>();
	private int numPoints = 0;
	private double nearestDistance = 0;
	private double exhaustiveDistance = 0;

	public double calculateDistance(Point start, Point end) {
		double xDist = end.getX() - start.getX();
		double yDist = end.getY() - start.getY(); 
		return Math.sqrt((xDist*xDist) + (yDist*yDist));
	}

	public void calculateExDist(ArrayList<Point> points) {
		double distance = 0;
		for (int i = 1; i < points.size(); i++){
			distance += calculateDistance(points.get(i-1), points.get(i));
		}
		if ((distance <= exhaustiveDistance) || (exhaustiveDistance == 0)) {
			exhaustiveDistance = distance;
			exhaustiveOrder.clear();
			for (Point p : points) {
				exhaustiveOrder.add(p);
			}
		}
		
	}

	public ArrayList<Point> addRandomPoints(int totalPoints) {
		ArrayList<Point> points = new ArrayList<Point>();
		Random rand = new Random();
		numPoints = totalPoints;
		for (int i = 0; i < numPoints; i++) {
			points.add(new Point(rand.nextInt()%100, rand.nextInt()%100));
		}
		return points;
	}

	public ArrayList<Point> addPointsFromFile(String fileName) throws FileNotFoundException {
		ArrayList<Point> points = new ArrayList<Point>();
		Scanner scan = new Scanner(new FileReader(fileName));
		String numPointsString = scan.nextLine();
		numPoints = Integer.parseInt(numPointsString);
		while(scan.hasNextLine()) {
			String[] tempString = scan.nextLine().split(" ");
			Point tempPoint = new Point(Integer.parseInt(tempString[0]), Integer.parseInt(tempString[1]));
			points.add(tempPoint);
		}
		return points;
	}

	public ArrayList<Point> nearestNeighbor(ArrayList<Point> unVisited) {
		ArrayList<Point> pointOrder = new ArrayList<Point>();
		Point startPoint = unVisited.get(0);
		Point currentPoint = startPoint;
		unVisited.remove(0);
		pointOrder.add(currentPoint);
		while (unVisited.size() > 0) {
			Point nextPoint = unVisited.get(0);
			int index = 0;
			double distance = 0;
			for (int i = 0; i < unVisited.size(); i++) {
				double nextDistance = calculateDistance(currentPoint, unVisited.get(i));
				if ((nextDistance < distance)||(distance == 0)) {
					distance = nextDistance;
					nextPoint = unVisited.get(i);
					index = i;
				}
			}
			nearestDistance += distance;
			currentPoint = nextPoint;
			pointOrder.add(currentPoint);
			unVisited.remove(index);
			if (unVisited.size() == 0) {
				nearestDistance += calculateDistance(currentPoint, startPoint);
			}
		}
		pointOrder.add(startPoint);
		return pointOrder;
	}

	public void exhaustive(int np, ArrayList<Point> vp, Point startPoint) {

		if (np == 1) {
			calculateExDist(vp);
			return;
		}
		for (int i = 1; i < np; i++) {
			vp = swap(vp, i, np-1);
			exhaustive(np-1, vp, startPoint);
			vp = swap(vp, i, np-1);
		}
	}
	// swap the characters at indices i and j
	private ArrayList<Point> swap(ArrayList<Point> vp, int i, int j) {
		Point c;
		c = vp.get(i); vp.set(i, vp.get(j)); vp.set(j, c);
		return vp;
	}

	public void printOrder(ArrayList<Point> order) {
		System.out.println("Order of point traversal:");
		for (Point p : order) {
			System.out.println("(" + (int) p.getX() + ", " + (int) p.getY() + ")");
		}

	}

	public int getNumPoints() {
		return numPoints;
	}

	public double getNearestDistance() {
		return nearestDistance;
	}

	public ArrayList<Point> getExhaustiveOrder() {
		return exhaustiveOrder;
	}

	public double getExhaustiveDistance() {
		return exhaustiveDistance;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Traverse t = new Traverse();
		ArrayList<Point> points = t.addPointsFromFile("input2.txt");
		//ArrayList<Point> points = t.addRandomPoints(10);
		long startTime = System.nanoTime();
		ArrayList<Point> nearestOrder = t.nearestNeighbor(points);
		long endTime = System.nanoTime();
		double nearestDuration = (endTime - startTime) * .000001;

		points = t.addPointsFromFile("input2.txt");
		//points = t.addRandomPoints(10);
		Point startPoint = points.get(0);
		points.add(startPoint);
		long exhaustiveStartTime = System.nanoTime();
		t.exhaustive(t.getNumPoints(), points, startPoint);
		long exhaustiveEndTime = System.nanoTime();
		double exhaustiveDuration = (exhaustiveEndTime - exhaustiveStartTime) * .000001;

		System.out.println("Nearest neighbor traversal: ");
		t.printOrder(nearestOrder);
		System.out.println("Distance: " + t.getNearestDistance());
		System.out.println("Execution time: " + nearestDuration + " miliseconds");
		System.out.println("");

		System.out.println("Exhaustive traversal: ");
		t.printOrder(t.getExhaustiveOrder());
		System.out.println("Distance: " + t.getExhaustiveDistance());
		System.out.println("Execution time: " + exhaustiveDuration + " miliseconds");
		System.out.println("");
	}
}
