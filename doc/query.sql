INSERT INTO public.spectator (id, active, created_ts, last_modified_ts, name, address, enabled, interval_next_player,
                              port, timeout_join_spectate, timeout_set_ready, created_by, last_modified_by, server_id)
VALUES (nextval('spectator_id_seq'), TRUE, now(), now(), '', '172.16.83.220', TRUE, 5, 62514, 10, 5, NULL, NULL, 52);