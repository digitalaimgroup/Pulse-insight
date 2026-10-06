import UIKit
import Capacitor
import AppTrackingTransparency
import FBAudienceNetwork

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    private var didRequestTrackingAuthorization = false

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Meta Audience Network requires ATE before Google Mobile Ads SDK init (AdMob mediation).
        // Do not request ATT here — UIWindow is not key yet; request on didBecomeActive.
        updateMetaAdvertiserTrackingEnabled()
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Guideline 2.1: ATT framework must actually show the system prompt on iPhone AND iPad.
        // Request when notDetermined on first foreground; sync Meta ATE after it resolves.
        requestTrackingAuthorizationIfNeeded()
        updateMetaAdvertiserTrackingEnabled()
    }

    /// Show ATT when status is notDetermined (fresh install / reset tracking). Safe on iPad.
    private func requestTrackingAuthorizationIfNeeded() {
        guard #available(iOS 14, *) else { return }
        guard !didRequestTrackingAuthorization else { return }
        guard ATTrackingManager.trackingAuthorizationStatus == .notDetermined else {
            updateMetaAdvertiserTrackingEnabled()
            return
        }
        didRequestTrackingAuthorization = true
        // Slight delay so the key window / root VC is ready (required for ATT sheet on iPad).
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) { [weak self] in
            ATTrackingManager.requestTrackingAuthorization { _ in
                DispatchQueue.main.async {
                    self?.updateMetaAdvertiserTrackingEnabled()
                }
            }
        }
    }

    /// Google AdMob Meta mediation: set FBAdSettings ATE from ATT status before ads load.
    private func updateMetaAdvertiserTrackingEnabled() {
        if #available(iOS 14, *) {
            FBAdSettings.setAdvertiserTrackingEnabled(
                ATTrackingManager.trackingAuthorizationStatus == .authorized
            )
        } else {
            FBAdSettings.setAdvertiserTrackingEnabled(true)
        }
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }

    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool {
        // Called when the app was launched with a url. Feel free to add additional processing here,
        // but if you want the App API to support tracking app url opens, make sure to keep this call
        return ApplicationDelegateProxy.shared.application(app, open: url, options: options)
    }

    func application(_ application: UIApplication, continue userActivity: NSUserActivity, restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        // Called when the app was launched with an activity, including Universal Links.
        // Feel free to add additional processing here, but if you want the App API to support
        // tracking app url opens, make sure to keep this call
        return ApplicationDelegateProxy.shared.application(application, continue: userActivity, restorationHandler: restorationHandler)
    }

}
