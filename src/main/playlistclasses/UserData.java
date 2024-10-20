package main.playlistclasses;

import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import main.creatorclasses.artistclasses.Merch;

import java.util.ArrayList;
import java.util.HashMap;

public final class UserData {
    private UserInput user;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<SongInput> likedSongs = new ArrayList<>();
    private ArrayList<Playlist> followedPlaylists = new ArrayList<>();
    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<PodcastInput> podcasts = new ArrayList<>();
    private HashMap<Merch, String> merches = new HashMap<>();

    public UserData() {
    }

    public UserInput getUser() {
        return user;
    }

    public void setUser(final UserInput user) {
        this.user = user;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(final ArrayList<Playlist> userPlaylists) {
        this.playlists = userPlaylists;
    }

    public ArrayList<SongInput> getLikedSongs() {
        return likedSongs;
    }

    public void setLikedSongs(final ArrayList<SongInput> likedSongs) {
        this.likedSongs = likedSongs;
    }

    public ArrayList<Playlist> getFollowedPlaylists() {
        return followedPlaylists;
    }

    public void setFollowedPlaylists(final ArrayList<Playlist> followedPlaylists) {
        this.followedPlaylists = followedPlaylists;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(final ArrayList<Album> albums) {
        this.albums = albums;
    }

    public ArrayList<PodcastInput> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(final ArrayList<PodcastInput> podcasts) {
        this.podcasts = podcasts;
    }

    public HashMap<Merch, String> getMerches() {
        return merches;
    }

    public void setMerches(final HashMap<Merch, String> merches) {
        this.merches = merches;
    }
}
