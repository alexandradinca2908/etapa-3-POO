package fileio.input;

import java.util.ArrayList;

public final class PodcastInput {
    private String name;
    private String owner;
    private ArrayList<EpisodeInput> episodes;

    public PodcastInput() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public ArrayList<EpisodeInput> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(final ArrayList<EpisodeInput> episodes) {
        this.episodes = episodes;
    }

    /**
     * This method calculates the duration of all episodes in the podcast
     *
     * @return The duration of the podcast
     */
    public int getDuration() {
        int duration = 0;
        for (EpisodeInput episode : this.episodes) {
            duration += episode.getDuration();
        }
        return duration;
    }
}
