import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import edu.upenn.cis350.gpx.*;



public class GPXcalculatorTest {
	final double DELTA_DEFAULT = 1e-12;
	
	
	//Error handling: GPXTrk is null
	@Test public void testNullTrk()
	{
		assertEquals( -1, GPXcalculator.calculateDistanceTraveled(null), DELTA_DEFAULT );
	}
	
	//Error handling: list of segments is null or list of segments contains no segments 
	@Test public void testNoSegs()
	{
		GPXtrk trk = new GPXtrk("a track", null);
		assertEquals( -1, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
		
		ArrayList<GPXtrkseg >segs = new ArrayList<GPXtrkseg>();
		trk = new GPXtrk("a track", segs);
		assertEquals( -1, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
	}
	
	//Boundary condition: the track contains at least one null segment.
	@Test public void testNullSegs()
	{
		ArrayList<GPXtrkseg >segs = new ArrayList<GPXtrkseg>();
		segs.add(null);
		GPXtrk trk = new GPXtrk("a track", segs);
		assertEquals( 0, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
		
		ArrayList<GPXtrkpt> pts = new ArrayList<GPXtrkpt>();
		pts.add( new GPXtrkpt(0,0,new Date(0) ) );
		pts.add( new GPXtrkpt(0,0,new Date(0) ) );
		segs.add( new GPXtrkseg( pts ) );
		trk = new GPXtrk("a track", segs);
		assertEquals( 0, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
	}
	
	//Boundary condition: segments have no points.
	@Test public void testSegNoPts()
	{
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add( new GPXtrkseg( null ) );
		GPXtrk trk = new GPXtrk("a track", segs);
		assertEquals( 0, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
		
		ArrayList<GPXtrkpt> pts = new ArrayList<GPXtrkpt>();
		segs = new ArrayList<GPXtrkseg>();
		segs.add( new GPXtrkseg( pts ) );
		trk = new GPXtrk("a track", segs);
		assertEquals( 0, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
	}
	
	//Boundary condition: a segment only has one point.
	@Test public void testOnePt()
	{
		ArrayList<GPXtrkseg >segs = new ArrayList<GPXtrkseg>();
		ArrayList<GPXtrkpt> pts = new ArrayList<GPXtrkpt>();
		pts.add( new GPXtrkpt( 10,0,new Date(0) ) );
		segs.add( new GPXtrkseg( pts ) );
		GPXtrk trk = new GPXtrk("a track", segs);
		assertEquals( 0, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
	}
	
	//Boundary conditions: a segment contains a null point
	@Test public void testNullPt()
	{
		ArrayList<GPXtrkseg >segs = new ArrayList<GPXtrkseg>();
		ArrayList<GPXtrkpt> pts = new ArrayList<GPXtrkpt>();
		pts.add( new GPXtrkpt( 10,0,new Date(0) ) );
		pts.add( null );
		segs.add( new GPXtrkseg( pts ) );
		GPXtrk trk = new GPXtrk("a track", segs);
		assertEquals( 0, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
	}
	
	//Error handling - latitude>90 or <-90, longitude>180 or <-180
	@Test public void testCoordinateBounds()
	{
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		ArrayList<GPXtrkpt> pts = new ArrayList<GPXtrkpt>();
		pts.add( new GPXtrkpt( 10,0,new Date(0) ) );
		pts.add( new GPXtrkpt( 91,0,new Date(1) ) );
		segs.add( new GPXtrkseg( pts ) );
		GPXtrk trk = new GPXtrk("a track", segs);
		assertEquals( 0, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
		
		segs = new ArrayList<GPXtrkseg>();
		pts = new ArrayList<GPXtrkpt>();
		pts.add( new GPXtrkpt( 10,-185,new Date(0) ) );
		pts.add( new GPXtrkpt( 0,0,new Date(1) ) );
		segs.add( new GPXtrkseg( pts ) );
		trk = new GPXtrk("a track", segs);
		assertEquals( 0, GPXcalculator.calculateDistanceTraveled(trk), DELTA_DEFAULT );
	}
	
	//Normal operation
	@Test public void testDistance()
	{
		ArrayList<GPXtrkpt> pts0 = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkseg >segs = new ArrayList<GPXtrkseg>();
		pts0.add( new GPXtrkpt(0,0,new Date(0) ) );
		pts0.add( new GPXtrkpt(10,10,new Date(1) ) );
		segs.add( new GPXtrkseg( pts0 ) );
		GPXtrk trk = new GPXtrk("a track", segs);
		assertEquals( 14.142, GPXcalculator.calculateDistanceTraveled(trk), 0.01 );
		
		ArrayList<GPXtrkpt> pts1 = new ArrayList<GPXtrkpt>();
		pts1.add( new GPXtrkpt(5,0,new Date(0) ) );
		pts1.add( new GPXtrkpt(-20,-10,new Date(1) ) );
		pts1.add( new GPXtrkpt(-30,-10,new Date(1) ) );
		segs.add( new GPXtrkseg( pts1 ) );
		trk = new GPXtrk("a track", segs);
		assertEquals( 51.068, GPXcalculator.calculateDistanceTraveled(trk), 0.01 );
	}
	

}
