package com.massisframework.massis3.examples.simulator.Components;


public final class Globals {
    public enum TAGS {
        PEDESTRIAN("Pedestrian"),
        TRAFFIC_CONTROLL("TrafficControl");

        private final String text;

        /**
         * @param text
         */
        private TAGS(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }
}
