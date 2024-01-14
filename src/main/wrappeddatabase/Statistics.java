package main.wrappeddatabase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.LibraryInput;
import fileio.input.UserInput;
import main.commandhelper.Command;
import main.monetization.PremiumUser;
import main.playlistclasses.Album;
import main.selectionclasses.ItemSelection;
import main.selectionclasses.PodcastSelection;
import main.utilityclasses.Constants;
import main.wrappeddatabase.alluserstats.ArtistStatistics;
import main.wrappeddatabase.alluserstats.HostStatistics;
import main.wrappeddatabase.alluserstats.UserStatistics;

import java.util.ArrayList;
import java.util.Map;

import static main.Main.updatePlayer;

public final class Statistics {
    private ArrayList<UserStatistics> usersStatistics;
    private ArrayList<ArtistStatistics> artistsStatistics;
    private ArrayList<HostStatistics> hostsStatistics;

    private static Statistics wrappedStats;

    private Statistics() {
    }

    /**
     * Return or create Singleton variable
     * @return Singleton Database regarding Wrapped Statistics
     */
    public static Statistics getWrappedStats() {
        if (wrappedStats == null) {
            wrappedStats = new Statistics();
            wrappedStats.usersStatistics = new ArrayList<>();
            wrappedStats.artistsStatistics = new ArrayList<>();
            wrappedStats.hostsStatistics = new ArrayList<>();
        }
        return wrappedStats;
    }

    /**
     * Resets all Wrapped fields
     */
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
