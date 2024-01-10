package main.WrappedDatabase;

import main.WrappedDatabase.AllUserStats.ArtistStatistics;
import main.WrappedDatabase.AllUserStats.HostStatistics;
import main.WrappedDatabase.AllUserStats.UserStatistics;

import java.util.ArrayList;

public final class Statistics {
    private ArrayList<UserStatistics> usersStatistics;
    private ArrayList<ArtistStatistics> artistsStatistics;
    private ArrayList<HostStatistics> hostsStatistics;

    private static Statistics wrappedStats;

    private Statistics(){
    }

    public static Statistics getWrappedStats() {
        if (wrappedStats == null) {
            wrappedStats = new Statistics();
            wrappedStats.usersStatistics = new ArrayList<>();
            wrappedStats.artistsStatistics = new ArrayList<>();
            wrappedStats.hostsStatistics = new ArrayList<>();
        }
        return wrappedStats;
    }

    public void resetWrappedStats() {
        wrappedStats.usersStatistics = null;
        wrappedStats.artistsStatistics = null;
        wrappedStats.hostsStatistics = null;
        wrappedStats = null;
    }

    public ArrayList<UserStatistics> getUsersStatistics() {
        return usersStatistics;
    }

    public void setUsersStatistics(final ArrayList<UserStatistics> usersStatistics) {
        this.usersStatistics = usersStatistics;
    }

    public ArrayList<ArtistStatistics> getArtistsStatistics() {
        return artistsStatistics;
    }

    public void setArtistsStatistics(final ArrayList<ArtistStatistics> artistsStatistics) {
        this.artistsStatistics = artistsStatistics;
    }

    public ArrayList<HostStatistics> getHostsStatistics() {
        return hostsStatistics;
    }

    public void setHostsStatistics(final ArrayList<HostStatistics> hostsStatistics) {
        this.hostsStatistics = hostsStatistics;
    }
}
