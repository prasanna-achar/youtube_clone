import React, { useRef, useEffect, useState } from "react";
import videojs from "video.js";
import "video.js/dist/video-js.css";

export default function VideoPlayer({ sources }) {
  const videoRef = useRef(null);
  const playerRef = useRef(null);
  const [quality, setQuality] = useState("auto");

  useEffect(() => {
    if (!playerRef.current && videoRef.current) {
      playerRef.current = videojs(videoRef.current, {
        controls: true,
        autoplay: false,
      
        preload: "auto",
        responsive: true,
        fluid: true,
        playbackRates: [0.5, 1, 1.25, 1.5, 2],
        userActions:{
          hotkeys:true
        },
        html5: {
          vhs: {
            overrideNative: true,
          },
        },
      });
    }
    console.log(playerRef.current)

    
    
  }, []);

  // When quality changes
  useEffect(() => {
    if (!playerRef.current || !sources) return;

    const selectedUrl = sources[quality];
    if (!selectedUrl) return;
    
    const player = playerRef.current;
    const currentTime = player.currentTime();
    const wasPlaying = !player.paused();

    player.src({
      src: selectedUrl,
      type: "application/x-mpegURL",
    });

    player.one("loadedmetadata", () => {
      player.currentTime(currentTime);
      if (wasPlaying) player.play().catch(() => {});
    });

  }, [quality, sources]);

  return (
    <div className="rounded-md">
      {/* Quality Picker */}
      <select
        value={quality}
        onChange={(e) => setQuality(e.target.value)}
        style={{
          padding: "4px 6px",
          marginBottom: "10px",
          borderRadius: "5px",
        }}
      >
        {Object.keys(sources)
          .filter((q) => sources[q])
          .map((q) => (
            <option key={q} value={q}>
              {q.toUpperCase()}
            </option>
          ))}
      </select>

      <video
        ref={videoRef}
        className="video-js vjs-big-play-centered rounded-md"
        style={{
          borderRadius:"15px"
        }}
        playsInline
      />
    </div>
  );
}
