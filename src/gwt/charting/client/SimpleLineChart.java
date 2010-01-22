package gwt.charting.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class SimpleLineChart extends Widget implements RequiresResize {

	private static SimpleLineChartUiBinder uiBinder = GWT.create(SimpleLineChartUiBinder.class);

	interface SimpleLineChartUiBinder extends
			UiBinder<Element, SimpleLineChart> {
	}

	@UiField
	DivElement graphHolder;

	@UiField
	CanvasElement graphCanvas;

	@UiField
	DivElement slider;

	@UiField
	DivElement loadingImageHolder;

	public SimpleLineChart() {
		setElement(uiBinder.createAndBindUi(this));
	}

	@Override
	protected void onAttach() {
		graphCanvas.setAttribute("width", "150");
		graphCanvas.setAttribute("height", "150");
		super.onAttach();
	}

	@Override
	protected void onLoad() {
		initializeCanvas(graphHolder, graphCanvas, slider, loadingImageHolder);
		super.onLoad();
	}

	/**
	 * Initialize canvas element - add zoom and slide supoort.
	 * 
	 * @param canvasElement
	 *            the canvas element to be initialized.
	 */
	private native void initializeCanvas(Element graphHolderElement, Element canvasElement, Element sliderElement, Element loadingImageHolderElement) /*-{
		var zoom = new $wnd.Zoom();
		
		var graphElement = canvasElement;
		var graphContext = graphElement.getContext("2d");
		
		$wnd.jQuery(graphElement).mousedown(function(e){
			zoom.start();
			zoom.setTopXY(e.pageX - graphElement.getBoundingClientRect().left, e.pageY - graphElement.getBoundingClientRect().top);
		});
		
		$wnd.jQuery(graphElement).mousemove(function(e){
			if (zoom.isActive()==true) {
				$wnd.clear(graphElement);
				$wnd.drawGridAndCurve(graphElement, graphContext, null, 0);
				zoom.setCurrentXY(e.pageX - graphElement.getBoundingClientRect().left, e.pageY - graphElement.getBoundingClientRect().top);
				graphContext.strokeStyle = "rgba(0, 0, 0, 0)";
				graphContext.strokeRect(zoom.getTopX(), zoom.getTopY(), zoom.getCurrentWidth(), zoom.getCurrentHeight());
				graphContext.fillStyle = "rgba(0, 0, 200, 0.1)";
				graphContext.fillRect(zoom.getTopX(), zoom.getTopY(), zoom.getCurrentWidth(), zoom.getCurrentHeight());
				graphContext.stroke();
			}
		});
		
		$wnd.jQuery(graphElement).mouseup(function(e){
			if (zoom.isActive() == true) {
				zoom.stop();
				$wnd.clear(graphElement);
				$wnd.drawGridAndCurve(graphElement, graphContext, null, 0);
			}
		});
		
		$wnd.jQuery.ajax({
			url: "measurementData?readingId=SUNCE0120091201113035939",
			dataType: "json",
			success: function(measurements) {
				loadingImageHolderElement.style.display='none';
				graphHolderElement.style.display='block';
				canvasElement.width = graphHolderElement.clientWidth - 10;
				$wnd.drawGridAndCurve(graphElement, graphContext, measurements, 0);
				$wnd.jQuery(sliderElement).slider({
					slide: function(event, ui) {
						$wnd.clear(graphElement);
						$wnd.drawGridAndCurve(graphElement, graphContext, null, ui.value * ((measurements.length - graphElement.width)/100));
					}
				});
			}
		});
	}-*/;

	private native void repaintCanvas(Element graphHolderElement, Element canvasElement, Element sliderElement, Element loadingImageHolderElement) /*-{
		canvasElement.width = graphHolderElement.clientWidth - 10; //add a little offset to the right of the canvas
		$wnd.drawGridAndCurve(canvasElement, canvasElement.getContext("2d"), null, 0);
	}-*/;

	@Override
	public void onResize() {
		repaintCanvas(graphHolder, graphCanvas, slider, loadingImageHolder);
	}

}
