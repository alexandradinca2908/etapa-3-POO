package main.likeclasses;

import fileio.input.SongInput;

public final class SongLikes implements Comparable<SongLikes> {
    private SongInput song;
    private int likes;

    @Override
    public int compareTo(final SongLikes songLikes) {
        return this.likes - songLikes.likes;
    }

    public SongLikes() {
    }

    public SongInput getSong() {
        return song;
    }

    public void setSong(final SongInput song) {
        this.song = song;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(final int likes) {
        this.likes = likes;
    }
}
