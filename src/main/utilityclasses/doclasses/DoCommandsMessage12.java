package main.utilityclasses.doclasses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import fileio.input.UserInput;
import main.commandhelper.Command;
import main.creatorclasses.artistclasses.Management;
import main.creatorclasses.hostclasses.HostInfo;
import main.creatorclasses.subscription.CreatorChannel;
import main.creatorclasses.subscription.NotificationBar;
import main.likeclasses.SongLikes;
import main.monetization.PremiumUser;
import main.pagingclasses.Page;
import main.playlistclasses.Album;
import main.playlistclasses.Playlist;
import main.playlistclasses.UserData;
import main.selectionclasses.ItemSelection;
import main.selectionclasses.PodcastSelection;
import main.utilityclasses.getmessageclasses.GetMessages12;

import java.util.ArrayList;

import static main.Main.updatePlayer;

import static main.utilityclasses.getmessageclasses.GetMessages12.getSwitchConnectionMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getRemovePodcastMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getRemoveEventMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getRemoveAnnouncementMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getRemoveAlbumMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getDeleteUserMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getChangePageMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getAddPodcastMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getAddMerchMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getAddEventMessage;
import static main.utilityclasses.getmessageclasses.GetMessages12.getAddAnnouncementMessage;

public final class DoCommandsMessage12 {
    private DoCommandsMessage12() {
    }

    /**
     * Main method call for doAddRemoveInPlaylist command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param player The array that keeps all user players in check
     * @param playlists The array of all user playlists
     * @param library Singleton containing all songs, users and podcasts
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doAddRemoveInPlaylist(final ObjectMapper objectMapper,
                                                   final Command crtCommand,
                                                   final ArrayList<ItemSelection> player,
                                                   final ArrayList<Playlist> playlists,
                                                   final LibraryInput library,
                                                   final ArrayList<PodcastSelection> podcasts,
                                                   final ArrayList<Album> albums,
                                                   final ArrayList<PremiumUser> premiumUsers) {
        ObjectNode addRemoveOutput = objectMapper.createObjectNode();

        addRemoveOutput.put("command", "addRemoveInPlaylist");
        addRemoveOutput.put("user", crtCommand.getUsername());
        addRemoveOutput.put("timestamp", crtCommand.getTimestamp());

        //  Update the player
        updatePlayer(player, crtCommand, podcasts, library, albums, premiumUsers);

        //  Check online status
        //  If user is offline, we exit the function before any action can be done
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(crtCommand.getUsername())) {
                if (!user.isOnline()) {
                    addRemoveOutput.put("message", user.getUsername() + " is offline.");
                    addRemoveOutput.putPOJO("results", new ArrayList<>());

                    return addRemoveOutput;
                }
            }
        }

        //  Get message and make proper modifications to the playlist
        String message = GetMessages12.getAddRemoveMessage(player, playlists, crtCommand,
                albums, premiumUsers);
        addRemoveOutput.put("message", message);

        return addRemoveOutput;
    }

    /**
     * Main method call for doLike command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param player The array that keeps all user players in check
     * @param usersData The array of users and their respective playlists
     * @param songsLikes The array of songs and their respective likes
     * @param library Singleton containing all songs, users and podcasts
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doLike(final ObjectMapper objectMapper,
                                    final Command crtCommand,
                                    final ArrayList<ItemSelection> player,
                                    final ArrayList<UserData> usersData,
                                    final ArrayList<SongLikes> songsLikes,
                                    final LibraryInput library,
                                    final ArrayList<Album> albums,
                                    final ArrayList<PremiumUser> premiumUsers) {
        ObjectNode likeOutput = objectMapper.createObjectNode();

        likeOutput.put("command", "like");
        likeOutput.put("user", crtCommand.getUsername());
        likeOutput.put("timestamp", crtCommand.getTimestamp());

        //  Check online status
        //  If user is offline, we exit the function before any action can be done
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(crtCommand.getUsername())) {
                if (!user.isOnline()) {
                    likeOutput.put("message", user.getUsername() + " is offline.");

                    return likeOutput;
                }
            }
        }

        //  Get message and make proper modifications to the user's liked songs
        String message = GetMessages12.getLikeMessage(player, usersData,
                crtCommand, songsLikes, albums, premiumUsers);

        likeOutput.put("message", message);

        return likeOutput;
    }

    /**
     * Main method call for doForward command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param podcasts The array that keeps track of all the podcasts
     *                  when they are not loaded
     * @param player The array that keeps all user players in check
     * @param library Singleton containing all songs, users and podcasts
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doForward(final ObjectMapper objectMapper,
                                       final Command crtCommand,
                                       final ArrayList<PodcastSelection> podcasts,
                                       final ArrayList<ItemSelection> player,
                                       final LibraryInput library,
                                       final ArrayList<Album> albums,
                                       final ArrayList<PremiumUser> premiumUsers) {
        ObjectNode forwardOutput = objectMapper.createObjectNode();

        forwardOutput.put("command", "forward");
        forwardOutput.put("user", crtCommand.getUsername());
        forwardOutput.put("timestamp", crtCommand.getTimestamp());

        //  First we update the player
        updatePlayer(player, crtCommand, podcasts, library, albums, premiumUsers);

        //  Check online status
        //  If user is offline, we exit the function before any action can be done
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(crtCommand.getUsername())) {
                if (!user.isOnline()) {
                    forwardOutput.put("message", user.getUsername() + " is offline.");
                    forwardOutput.putPOJO("results", new ArrayList<>());

                    return forwardOutput;
                }
            }
        }

        //  Now we check for podcast
        ItemSelection crtItem = null;

        for (ItemSelection item : player) {
            if (item.getUser().equals(crtCommand.getUsername())) {
                crtItem = item;
            }
        }

        //  Get message and make changes
        String message = GetMessages12.getForwardMessage(crtItem, crtCommand,
                player, podcasts, albums, premiumUsers);
        forwardOutput.put("message", message);

        return forwardOutput;
    }

    /**
     * Main method call for doBackward command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param podcasts The array that keeps track of all the podcasts
     *                  when they are not loaded
     * @param player The array that keeps all user players in check
     * @param library Singleton containing all songs, users and podcasts
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doBackward(final ObjectMapper objectMapper,
                                        final Command crtCommand,
                                        final ArrayList<PodcastSelection> podcasts,
                                        final ArrayList<ItemSelection> player,
                                        final LibraryInput library,
                                        final ArrayList<Album> albums,
                                        final ArrayList<PremiumUser> premiumUsers) {
        ObjectNode backwardOutput = objectMapper.createObjectNode();

        backwardOutput.put("command", "backward");
        backwardOutput.put("user", crtCommand.getUsername());
        backwardOutput.put("timestamp", crtCommand.getTimestamp());

        //  First we update the player
        updatePlayer(player, crtCommand, podcasts, library, albums, premiumUsers);

        //  Check online status
        //  If user is offline, we exit the function before any action can be done
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(crtCommand.getUsername())) {
                if (!user.isOnline()) {
                    backwardOutput.put("message", user.getUsername() + " is offline.");
                    backwardOutput.putPOJO("results", new ArrayList<>());

                    return backwardOutput;
                }
            }
        }

        //  Now we check for podcast
        ItemSelection crtItem = null;

        for (ItemSelection item : player) {
            if (item.getUser().equals(crtCommand.getUsername())) {
                crtItem = item;
            }
        }

        //  Get message and make changes
        String message = GetMessages12.getBackwardMessage(crtItem);
        backwardOutput.put("message", message);

        return backwardOutput;
    }

    /**
     * Main method call for doNext command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param podcasts The array that keeps track of all the podcasts
     *                  when they are not loaded
     * @param player The array that keeps all user players in check
     * @param library Singleton containing all songs, users and podcasts
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doNext(final ObjectMapper objectMapper,
                                    final Command crtCommand,
                                    final ArrayList<PodcastSelection> podcasts,
                                    final ArrayList<ItemSelection> player,
                                    final LibraryInput library,
                                    final ArrayList<Album> albums,
                                    final ArrayList<PremiumUser> premiumUsers) {
        ObjectNode nextOutput = objectMapper.createObjectNode();

        nextOutput.put("command", "next");
        nextOutput.put("user", crtCommand.getUsername());
        nextOutput.put("timestamp", crtCommand.getTimestamp());

        //  First we update the player
        updatePlayer(player, crtCommand, podcasts, library, albums, premiumUsers);

        //  Check online status
        //  If user is offline, we exit the function before any action can be done
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(crtCommand.getUsername())) {
                if (!user.isOnline()) {
                    nextOutput.put("message", user.getUsername() + " is offline.");
                    nextOutput.putPOJO("results", new ArrayList<>());

                    return nextOutput;
                }
            }
        }

        //  Now we check for loaded source
        ItemSelection crtItem = null;

        for (ItemSelection item : player) {
            if (item.getUser().equals(crtCommand.getUsername())) {
                crtItem = item;
            }
        }

        //  Get message and make changes
        String message = GetMessages12.getNextMessage(crtItem, player,
                podcasts, crtCommand);
        nextOutput.put("message", message);

        return nextOutput;
    }

    /**
     * Main method call for doPrev command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param podcasts The array that keeps track of all the podcasts
     *                  when they are not loaded
     * @param player The array that keeps all user players in check
     * @param library Singleton containing all songs, users and podcasts
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doPrev(final ObjectMapper objectMapper,
                                    final Command crtCommand,
                                    final ArrayList<PodcastSelection> podcasts,
                                    final ArrayList<ItemSelection> player,
                                    final LibraryInput library,
                                    final ArrayList<Album> albums,
                                    final ArrayList<PremiumUser> premiumUsers) {
        ObjectNode prevOutput = objectMapper.createObjectNode();

        prevOutput.put("command", "prev");
        prevOutput.put("user", crtCommand.getUsername());
        prevOutput.put("timestamp", crtCommand.getTimestamp());

        //  First we update the player
        updatePlayer(player, crtCommand, podcasts, library, albums, premiumUsers);

        //  Check online status
        //  If user is offline, we exit the function before any action can be done
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(crtCommand.getUsername())) {
                if (!user.isOnline()) {
                    prevOutput.put("message", user.getUsername() + " is offline.");
                    prevOutput.putPOJO("results", new ArrayList<>());

                    return prevOutput;
                }
            }
        }

        //  Now we check for loaded source
        ItemSelection crtItem = null;

        for (ItemSelection item : player) {
            if (item.getUser().equals(crtCommand.getUsername())) {
                crtItem = item;
            }
        }

        //  Get message and make changes
        String message = GetMessages12.getPrevMessage(crtItem, crtCommand);
        prevOutput.put("message", message);

        return prevOutput;
    }

    /**
     * Main method call for doSwitchVisibility command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param usersData The array of users and their respective playlists
     * @param library Singleton containing all songs, users and podcasts
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doSwitchVisibility(final ObjectMapper objectMapper,
                                                final Command crtCommand,
                                                final ArrayList<UserData> usersData,
                                                final LibraryInput library) {
        ObjectNode switchOutput = objectMapper.createObjectNode();

        switchOutput.put("command", "switchVisibility");
        switchOutput.put("user", crtCommand.getUsername());
        switchOutput.put("timestamp", crtCommand.getTimestamp());

        //  Check online status
        //  If user is offline, we exit the function before any action can be done
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(crtCommand.getUsername())) {
                if (!user.isOnline()) {
                    switchOutput.put("message", user.getUsername() + " is offline.");
                    switchOutput.putPOJO("results", new ArrayList<>());

                    return switchOutput;
                }
            }
        }

        //  Get message and make changes
        String message = GetMessages12.getSwitchVisibilityMessage(usersData,
                crtCommand);
        switchOutput.put("message", message);

        return switchOutput;
    }

    /**
     * Main method call for doSwitchConnectionStatus command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param player The array that keeps all user players in check
     * @param library Singleton containing all songs, users and podcasts
     * @param podcasts The array that keeps track of all the podcasts
     *                  when they are not loaded
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doSwitchConnectionStatus(final ObjectMapper objectMapper,
                                                      final Command crtCommand,
                                                      final ArrayList<ItemSelection> player,
                                                      final LibraryInput library,
                                                      final ArrayList<PodcastSelection> podcasts,
                                                      final ArrayList<Album> albums,
                                                      final ArrayList<PremiumUser> premiumUsers) {
        ObjectNode switchConnectionOutput = objectMapper.createObjectNode();

        switchConnectionOutput.put("command", "switchConnectionStatus");
        switchConnectionOutput.put("user", crtCommand.getUsername());
        switchConnectionOutput.put("timestamp", crtCommand.getTimestamp());

        UserInput crtUser = null;

        //  Searching for the current user
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(crtCommand.getUsername())) {
                crtUser = user;
                break;
            }
        }

        //  Get message and make changes
        String message = getSwitchConnectionMessage(crtUser,
                player, crtCommand, podcasts, library, albums,
                premiumUsers);
        switchConnectionOutput.put("message", message);

        return  switchConnectionOutput;
    }

    /**
     * Main method call for doAddUser command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param library Singleton containing all songs, users and podcasts
     * @param usersData The array of users and their respective playlists
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doAddUser(final ObjectMapper objectMapper,
                                       final Command crtCommand,
                                       final LibraryInput library,
                                       final ArrayList<UserData> usersData,
                                       final ArrayList<Page> pageSystem,
                                       final ArrayList<Management> managements,
                                       final ArrayList<HostInfo> hostInfos,
                                       final ArrayList<CreatorChannel> channels,
                                       final ArrayList<NotificationBar> notificationBars) {
        ObjectNode addUserOutput = objectMapper.createObjectNode();

        addUserOutput.put("command", "addUser");
        addUserOutput.put("user", crtCommand.getUsername());
        addUserOutput.put("timestamp", crtCommand.getTimestamp());

        String message = GetMessages12.getAddUserMessage(crtCommand,
                library, usersData, pageSystem, managements,
                hostInfos, channels, notificationBars);
        addUserOutput.put("message", message);

        return addUserOutput;
    }

    /**
     * Main method call for doAddAlbum command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param library Singleton containing all songs, users and podcasts
     * @param usersData The array of users and their respective playlists
     * @param albums The array of all albums in the database
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doAddAlbum(final ObjectMapper objectMapper,
                                        final Command crtCommand,
                                        final LibraryInput library,
                                        final ArrayList<UserData> usersData,
                                        final ArrayList<Album> albums,
                                        final ArrayList<SongLikes> songsLikes,
                                        final ArrayList<CreatorChannel> channels,
                                        final ArrayList<NotificationBar> notificationBars) {
        ObjectNode addAlbumOutput = objectMapper.createObjectNode();

        addAlbumOutput.put("command", "addAlbum");
        addAlbumOutput.put("user", crtCommand.getUsername());
        addAlbumOutput.put("timestamp", crtCommand.getTimestamp());

        String message = GetMessages12.getAddAlbumMessage(crtCommand, library,
                usersData, albums, songsLikes, channels, notificationBars);
        addAlbumOutput.put("message", message);

        return addAlbumOutput;
    }

    /**
     * Main method call for doAddEvent command
     *
     * @param objectMapper Object Mapper
     * @param library Singleton containing all songs, users and podcasts
     * @param crtCommand Current command
     * @param managements The array containing events and merch of all artists
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doAddEvent(final ObjectMapper objectMapper,
                                        final LibraryInput library,
                                        final Command crtCommand,
                                        final ArrayList<Management> managements,
                                        final ArrayList<CreatorChannel> channels,
                                        final ArrayList<NotificationBar> notificationBars) {
        ObjectNode addEventOutput = objectMapper.createObjectNode();

        addEventOutput.put("command", "addEvent");
        addEventOutput.put("user", crtCommand.getUsername());
        addEventOutput.put("timestamp", crtCommand.getTimestamp());

        String message = getAddEventMessage(library, crtCommand,
                managements, channels, notificationBars);

        addEventOutput.put("message", message);

        return addEventOutput;
    }

    /**
     * Main method call for doAddMerch command
     *
     * @param objectMapper Object Mapper
     * @param library Singleton containing all songs, users and podcasts
     * @param crtCommand Current command
     * @param managements The array containing events and merch of all artists
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doAddMerch(final ObjectMapper objectMapper,
                                        final LibraryInput library,
                                        final Command crtCommand,
                                        final ArrayList<Management> managements,
                                        final ArrayList<CreatorChannel> channels,
                                        final ArrayList<NotificationBar> notificationBars) {
        ObjectNode addMerchOutput = objectMapper.createObjectNode();

        addMerchOutput.put("command", "addMerch");
        addMerchOutput.put("user", crtCommand.getUsername());
        addMerchOutput.put("timestamp", crtCommand.getTimestamp());

        String message = getAddMerchMessage(library, crtCommand,
                managements, channels, notificationBars);

        addMerchOutput.put("message", message);

        return addMerchOutput;
    }

    /**
     * Main method call for doDeleteUser command
     *
     * @param objectMapper Object Mapper
     * @param library Singleton containing all songs, users and podcasts
     * @param crtCommand Current command
     * @param playlists The array of all user playlists
     * @param usersData The array of users and their respective playlists
     * @param albums The array of all albums in the database
     * @param songsLikes The array of songs and their respective likes
     * @param player The array that keeps all user players in check
     * @param podcasts The array that keeps track of all the podcasts
     * @param pageSystem Array of all the pages in the system
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doDeleteUser(final ObjectMapper objectMapper,
                                          final LibraryInput library,
                                          final Command crtCommand,
                                          final ArrayList<ItemSelection> player,
                                          final ArrayList<Playlist> playlists,
                                          final ArrayList<UserData> usersData,
                                          final ArrayList<Album> albums,
                                          final ArrayList<SongLikes> songsLikes,
                                          final ArrayList<PodcastSelection> podcasts,
                                          final ArrayList<Page> pageSystem,
                                          final ArrayList<PremiumUser> premiumUsers) {
        ObjectNode deleteUserOutput = objectMapper.createObjectNode();

        deleteUserOutput.put("command", "deleteUser");
        deleteUserOutput.put("user", crtCommand.getUsername());
        deleteUserOutput.put("timestamp", crtCommand.getTimestamp());

        //  Update the player
        updatePlayer(player, crtCommand, podcasts, library, albums, premiumUsers);

        String message = getDeleteUserMessage(library, crtCommand,
                player, playlists, usersData, albums,
                songsLikes, pageSystem, podcasts);


        deleteUserOutput.put("message", message);

        return deleteUserOutput;
    }

    /**
     * Main method call for doAddPodcast command
     *
     * @param objectMapper Object Mapper
     * @param library Singleton containing all songs, users and podcasts
     * @param crtCommand Current command
     * @param usersData The array of users and their respective playlists
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doAddPodcast(final ObjectMapper objectMapper,
                                          final Command crtCommand,
                                          final LibraryInput library,
                                          final ArrayList<UserData> usersData,
                                          final ArrayList<CreatorChannel> channels,
                                          final ArrayList<NotificationBar> notificationBars) {
        ObjectNode addPodcastOutput = objectMapper.createObjectNode();

        addPodcastOutput.put("command", "addPodcast");
        addPodcastOutput.put("user", crtCommand.getUsername());
        addPodcastOutput.put("timestamp", crtCommand.getTimestamp());

        String message = getAddPodcastMessage(library, crtCommand,
                usersData, channels, notificationBars);
        addPodcastOutput.put("message", message);

        return addPodcastOutput;
    }

    /**
     * Main method call for doAddAnnouncement command
     *
     * @param objectMapper Object Mapper
     * @param library Singleton containing all songs, users and podcasts
     * @param crtCommand Current command
     * @param hostInfos The array containing all announcements for every artist
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doAddAnnouncement(final ObjectMapper objectMapper,
                                               final Command crtCommand,
                                               final LibraryInput library,
                                               final ArrayList<HostInfo> hostInfos,
                                               final ArrayList<CreatorChannel> channels,
                                               final ArrayList<NotificationBar> notificationBars) {
        ObjectNode addAnnouncementOutput = objectMapper.createObjectNode();

        addAnnouncementOutput.put("command", "addAnnouncement");
        addAnnouncementOutput.put("user", crtCommand.getUsername());
        addAnnouncementOutput.put("timestamp", crtCommand.getTimestamp());

        String message = getAddAnnouncementMessage(library,
                crtCommand, hostInfos, channels, notificationBars);
        addAnnouncementOutput.put("message", message);

        return addAnnouncementOutput;
    }

    /**
     * Main method call for doRemoveAnnouncement command
     *
     * @param objectMapper Object Mapper
     * @param library Singleton containing all songs, users and podcasts
     * @param crtCommand Current command
     * @param hostInfos The array containing all announcements for every artist
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doRemoveAnnouncement(final ObjectMapper objectMapper,
                                                  final Command crtCommand,
                                                  final LibraryInput library,
                                                  final ArrayList<HostInfo> hostInfos) {
        ObjectNode removeAnnouncementOutput = objectMapper.createObjectNode();

        removeAnnouncementOutput.put("command", "removeAnnouncement");
        removeAnnouncementOutput.put("user", crtCommand.getUsername());
        removeAnnouncementOutput.put("timestamp", crtCommand.getTimestamp());

        String message = getRemoveAnnouncementMessage(library,
                crtCommand, hostInfos);

        removeAnnouncementOutput.put("message", message);

        return removeAnnouncementOutput;
    }

    /**
     * Main method call for doRemoveAlbum command
     *
     * @param objectMapper Object Mapper
     * @param library Singleton containing all songs, users and podcasts
     * @param crtCommand Current command
     * @param playlists The array of all user playlists
     * @param usersData The array of users and their respective playlists
     * @param albums The array of all albums in the database
     * @param songsLikes The array of songs and their respective likes
     * @param player The array that keeps all user players in check]
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doRemoveAlbum(final ObjectMapper objectMapper,
                                           final Command crtCommand,
                                           final LibraryInput library,
                                           final ArrayList<UserData> usersData,
                                           final ArrayList<ItemSelection> player,
                                           final ArrayList<Playlist> playlists,
                                           final ArrayList<SongLikes> songsLikes,
                                           final ArrayList<Album> albums) {
        ObjectNode removeAlbumOutput = objectMapper.createObjectNode();

        removeAlbumOutput.put("command", "removeAlbum");
        removeAlbumOutput.put("user", crtCommand.getUsername());
        removeAlbumOutput.put("timestamp", crtCommand.getTimestamp());

        String message = getRemoveAlbumMessage(library, crtCommand,
                usersData, player, playlists, songsLikes,
                albums);

        removeAlbumOutput.put("message", message);

        return removeAlbumOutput;
    }

    /**
     * Main method call for doChangePage command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param pageSystem Array of all the pages in the system
     * @param usersData The array of users and their respective playlists
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doChangePage(final ObjectMapper objectMapper,
                                          final Command crtCommand,
                                          final ArrayList<Page> pageSystem,
                                          final ArrayList<UserData> usersData) {
        ObjectNode changePageOutput = objectMapper.createObjectNode();

        changePageOutput.put("command", "changePage");
        changePageOutput.put("user", crtCommand.getUsername());
        changePageOutput.put("timestamp", crtCommand.getTimestamp());

        String message = getChangePageMessage(crtCommand,
                usersData, pageSystem);

        changePageOutput.put("message", message);

        return changePageOutput;
    }

    /**
     * Main method call for doRemovePodcast command
     *
     * @param objectMapper Object Mapper
     * @param crtCommand Current command
     * @param usersData The array of users and their respective playlists
     * @param player The array that keeps all user players in check]
     * @param library Singleton containing all songs, users and podcasts
     * @param podcasts The array that keeps track of all the podcasts
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doRemovePodcast(final ObjectMapper objectMapper,
                                             final Command crtCommand,
                                             final ArrayList<UserData> usersData,
                                             final ArrayList<ItemSelection> player,
                                             final LibraryInput library,
                                             final ArrayList<PodcastSelection> podcasts) {
        ObjectNode removePodcastOutput = objectMapper.createObjectNode();

        removePodcastOutput.put("command", "removePodcast");
        removePodcastOutput.put("user", crtCommand.getUsername());
        removePodcastOutput.put("timestamp", crtCommand.getTimestamp());

        String message = getRemovePodcastMessage(crtCommand,
                usersData, player, library, podcasts);

        removePodcastOutput.put("message", message);

        return removePodcastOutput;
    }

    /**
     * Main method call for doRemoveEvent command
     *
     * @param objectMapper Object Mapper
     * @param library Singleton containing all songs, users and podcasts
     * @param crtCommand Current command
     * @param managements The array containing events and merch of all artists
     * @return ObjectNode of the final JSON
     */
    public static ObjectNode doRemoveEvent(final ObjectMapper objectMapper,
                                           final Command crtCommand,
                                           final LibraryInput library,
                                           final ArrayList<Management> managements) {
        ObjectNode removeEventOutput = objectMapper.createObjectNode();

        removeEventOutput.put("command", "removeEvent");
        removeEventOutput.put("user", crtCommand.getUsername());
        removeEventOutput.put("timestamp", crtCommand.getTimestamp());

        String message = getRemoveEventMessage(crtCommand,
                library, managements);

        removeEventOutput.put("message", message);

        return removeEventOutput;
    }

}
