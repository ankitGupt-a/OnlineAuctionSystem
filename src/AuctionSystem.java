import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuctionSystem {
    private static final AuctionSystem instance = new AuctionSystem();
    private final Map<String, AuctionEvent> auctionsEvents;
    ScheduledExecutorService scheduler;

    private AuctionSystem() {
        auctionsEvents = new ConcurrentHashMap<>();
        scheduler = Executors.newScheduledThreadPool(5);
    }

    public static AuctionSystem getInstance() {
        return instance;
    }

    public void createAuction(final AuctionEvent auctionEvent) {
        auctionsEvents.put(auctionEvent.getId(), auctionEvent);
        scheduleAuctionEnd(auctionEvent);
    }

    public void placeBid(final AuctionEvent auctionEvent, final User user, final long bid) {
        if (!auctionsEvents.containsKey(auctionEvent.getId())) {
            System.out.println("No such auction");
            return;
        }

        auctionEvent.placeBid(user, bid);
    }
    public List<AuctionEvent> searchAuction(final String keyword) {
        List<AuctionEvent> allAuctions = new ArrayList<>();

        for (AuctionEvent auctionEvent: auctionsEvents.values()) {
            if (auctionEvent.getName().contains(keyword) || auctionEvent.getItem().getName().contains(keyword)) {
                allAuctions.add(auctionEvent);
            }
        }

        return allAuctions;
    }

    private void scheduleAuctionEnd(final AuctionEvent auctionEvent) {
        long delay = Duration.between(LocalDateTime.now(),
                auctionEvent.getStartingTime().plusSeconds(auctionEvent.getDuration())).toMillis();
        scheduler.schedule(auctionEvent::closeAuction, delay, TimeUnit.MILLISECONDS);
    }

    public void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (scheduler.awaitTermination(5, TimeUnit.MILLISECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
