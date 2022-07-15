package MyProjectGradle.service;

import MyProjectGradle.models.views.StatsViewModel;

public interface StatService {
  void onRequest();
  StatsViewModel getStats();
}
