import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class ChartFrame extends JFrame implements Runnable{
	private int counter = 0;
	final XYSeries series1 = new XYSeries("First");
	JFreeChart chart = null;
	
	public ChartFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void setData(float value){
		series1.add(counter,value);
		counter++;
	}
	private XYDataset createDataset() {

		//series1.add(1.0, 1.0);

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);

		return dataset;
	}

	private JFreeChart createChart(final XYDataset dataset) {

		// create the chart...
		chart = ChartFactory.createXYLineChart(
				"Temperatura",      // chart title
				"Tempo",                      // x axis label
				"Temperatura",                      // y axis label
				dataset,                  // data
				PlotOrientation.VERTICAL,
				true,                     // include legend
				true,                     // tooltips
				false                     // urls
		);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		//        final StandardLegend legend = (StandardLegend) chart.getLegend();
		//      legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		//    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}
	@Override
	public void run() {
		final XYDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		setSize(500,500);
		setVisible(true);
	}


}
