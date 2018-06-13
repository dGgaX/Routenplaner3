package de.abring.routenplaner.gui.components.mapTiles;

import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

public class MyTileSource {

	public static final String MAP_TILEHOSTER_POSITRON = "https://maps.tilehosting.com/styles/positron";
        public static final String MAP_TILEHOSTER_POSITRON_KEY = "?key=grAzG6EzmHIeuIFYiZK4";
        
        public static final String MAP_TILEHOSTER_DARK = "https://maps.tilehosting.com/styles/darkmatter";
        public static final String MAP_TILEHOSTER_DARK_KEY = "?key=hWWfWrAiWGtv68r8wA6D";
        
	protected static abstract class AbstractOsmTileSource implements TileSource {

		public int getMaxZoom() {
			return 18;
		}

		public int getMinZoom() {
                    return 0;
                }

		public String getTileUrl(int zoom, int tilex, int tiley) {
			return "/" + zoom + "/" + tilex + "/" + tiley + ".png";
		}

		@Override
		public String toString() {
			return getName();
		}

		public String getTileType() {
			return "png";
		}
		
	}

	public static class TILEHOSTER_POSITRON extends AbstractOsmTileSource {

		public static String NAME = "Positron";
		
		public String getName() {
			return NAME;
		}

		@Override
		public String getTileUrl(int zoom, int tilex, int tiley) {
			return MAP_TILEHOSTER_POSITRON + super.getTileUrl(zoom, tilex, tiley) + MAP_TILEHOSTER_POSITRON_KEY;
		}

		public TileUpdate getTileUpdate() {
			return TileUpdate.IfNoneMatch;
		}

	}
        
        public static class TILEHOSTER_DARK extends AbstractOsmTileSource {

		public static String NAME = "Dark Matter";
		
		public String getName() {
			return NAME;
		}

		@Override
		public String getTileUrl(int zoom, int tilex, int tiley) {
			return MAP_TILEHOSTER_DARK + super.getTileUrl(zoom, tilex, tiley) + MAP_TILEHOSTER_DARK_KEY;
		}

		public TileUpdate getTileUpdate() {
			return TileUpdate.IfNoneMatch;
		}

	}
}
