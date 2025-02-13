package ai.rounds.speedmeter.models;

import java.util.UUID;

/**
 * Model representing a session
 */
public class Session {
    
    /**
     * The session's id represented by a UUID string.
     */
    private final String id;
    /**
     * The timestamp on which the session started.
     */
    private final long startTime;
    /**
     * The timestamp on which the session ended.
     */
    private long endTime;
    /**
     * The total distance traveled of the session
     */
    private float distance;
    /**
     * The total distance of the session
     */
    private float averageSpeed;
    /**
     *  The latitude on witch the session started.
     */
    private double startLatitude;
    /**
     * The longitude on witch the session started.
     */
    private double startLongitude;
    /**
     *  The latitude on witch the session ended.
     */
    private double endLatitude;
    /**
     * The longitude on witch the session ended.
     */
    private double endLongitude;

    /**
     * Constructor
     *
     * @param creationTimestamp The session's creation timestamp in millis
     */
    public Session(long creationTimestamp) {
        this.id = UUID.randomUUID().toString();
        this.startTime = creationTimestamp;
        this.endTime = -1;
    }

    /**
     * Constructor
     *
     * @param id        The session's id
     * @param startTime The session's start timestamp
     * @param endTime   The session's end timestamp
     */
    public Session(String id, long startTime, long endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    /**
     * Gets the session's start timestamp
     *
     * @return The session's start timestamp in millis
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets the session's end timestamp
     *
     * @return The session's end timestamp in millis
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Sets the session's end timestamp in millis
     *
     * @param endTime The session end timestamp to be set
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the session's traveled distance
     *
     * @return The session's traveled distance
     */
    public float getDistance() {
        return distance;
    }

    /**
     * Sets the session's traveled distance
     *
     * @param distance The distance to be set
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }

    /**
     * Gets the session's average speed
     *
     * @return The session's average speed
     */
    public float getAverageSpeed() {
        return averageSpeed;
    }

    /**
     * Gets the session's start latitude
     * @return the session`s start latitude
     */
    public double getStartLatitude() {
        return startLatitude;
    }

    /**
     * Sets the session's start latitude
     * @param startLatitude The starting latitude to be set
     */
    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    /**
     * Gets the session's start longitude
     * @return the session`s start longitude
     */
    public double getStartLongitude() {
        return startLongitude;
    }

    /**
     * Sets the session's start longitude
     * @param startLongitude The starting longitude to be set
     */
    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    /**
     * Gets the session's end latitude
     * @return the session`s end latitude
     */
    public double getEndLatitude() {
        return endLatitude;
    }

    /**
     * Sets the session's end latitude
     * @param endLatitude The ending latitude to be set
     */
    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    /**
     * Gets the session's edn longitude
     * @return the session`s end longitude
     */
    public double getEndLongitude() {
        return endLongitude;
    }


    /**
     * Sets the session's end longitude
     * @param endLongitude The ending longitude to be set
     */
    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    /**
     * Sets the session's average speed
     *
     * @param averageSpeed The end timestamp to be set
     */
    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    @Override
    public String toString() {
        return "TrackingSession{" +
                "mId='" + id + '\'' +
                ", mStartTime=" + startTime +
                ", mEndTime=" + endTime +
                ", mDistance=" + distance +
                ", mAverageSpeed=" + averageSpeed +
                ", startLocation=" + startLatitude + " " + startLongitude +
                ", endLocation=" + endLatitude + " " + endLongitude +
                '}';
    }
}
