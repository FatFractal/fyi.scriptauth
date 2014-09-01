//
//  ViewController.m
//  FF-IOS-ScriptAuthTest
//
//  Created by David Wells on 2/17/13.
//  Copyright (c) 2013 FatFractal. All rights reserved.
//

#import <FFEF/FatFractal.h>
#import "ViewController.h"
#import "WebViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    // Check out cookies
    NSArray *cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    for (NSHTTPCookie *cookie in cookies) {
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] deleteCookie:cookie];
    }
    
    self.loggedInView.hidden = YES;
    self.errorLabel.hidden = YES;
    self.navigationController.navigationBarHidden = YES;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setErrorText:(NSString *)errorText
{
    if (errorText && errorText.length > 0) {
        self.errorLabel.text = errorText;
        [self.errorLabel sizeToFit];
        self.errorLabel.hidden = NO;
    } else {
        self.errorLabel.hidden = YES;
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    FatFractal *ff = [FatFractal main];
    NSString *callbackUri;
    NSString *authUri;
    NSString *scriptAuthService;
    if ([sender isEqual:self.facebookButton]) {
        scriptAuthService = FF_SCRIPT_AUTH_SERVICE_FACEBOOK;
        callbackUri = @"x-facebook-ff://authorize";
        [ff setCallbackUri:callbackUri forScriptAuthService:scriptAuthService];
        
        NSError *err;
        authUri = [ff authUriForScriptAuthService:scriptAuthService error:&err];
        if (err) {
            NSLog(@"ERROR: %@", err.localizedDescription);
            [self setErrorText:err.localizedDescription];
        }
        NSLog(@"authUri = %@", authUri);
    } else {
        scriptAuthService = FF_SCRIPT_AUTH_SERVICE_TWITTER;
        callbackUri = @"x-twitter-ff://authorize";
        [ff setCallbackUri:callbackUri forScriptAuthService:scriptAuthService];
        
        NSError *err;
        authUri = [ff authUriForScriptAuthService:scriptAuthService error:&err];
        if (err) {
            NSLog(@"ERROR: %@", err.localizedDescription);
            [self setErrorText:err.localizedDescription];
        }
        NSLog(@"authUri = %@", authUri);
    }
    
    UINavigationController *vc = segue.destinationViewController;
    WebViewController *wvc = (WebViewController *)vc.topViewController;
    wvc.scriptAuthService = scriptAuthService;
    wvc.callbackUri = callbackUri;
    wvc.authUri = authUri;
    wvc.delegate = self;
}

- (IBAction)logOut:(id)sender
{
    [[FatFractal main] logout];
    self.loggedOutView.hidden = NO;
    self.loggedInView.hidden = YES;
}

- (void)webViewController:(WebViewController *)webViewController didReceiveCallbackUriWithCode:(NSString *)callbackUriWithCode
{
    NSLog(@"Received callbackUriWithCode: %@", callbackUriWithCode);
    NSString *service = webViewController.scriptAuthService;
    FatFractal *ff = [FatFractal main];
    
    [ff retrieveAccessTokenForScriptAuthService:service callbackUriWithVerifier:callbackUriWithCode onComplete:^(NSError *theErr, id theObj, NSHTTPURLResponse *theResponse) {
        if (theErr) {
            NSLog(@"ERROR: %@", theErr.localizedDescription);
            [self setErrorText:theErr.localizedDescription];
        } else {
            NSLog(@"SUCCESS!");
            [ff loginWithScriptAuthService:service onComplete:^(NSError *theErr, id theObj, NSHTTPURLResponse *theResponse) {
                if (theErr) {
                    NSLog(@"ERROR: %@", theErr.localizedDescription);
                    [self setErrorText:theErr.localizedDescription];
                } else {
                    NSLog(@"SUCCESS AGAIN! user = %@", theObj);
                    FFUser *user = theObj;
                    
                    self.loggedOutView.hidden = YES;
                    self.loggedInView.hidden = NO;
                    self.userNameLabel.text = user.userName;
                    self.serviceLabel.text = user.scriptAuthService;
                    
                    [self.userNameLabel sizeToFit];
                    [self.serviceLabel sizeToFit];
                }
            }];
        }
    }];
}

- (void)webViewControllerDidCancel:(WebViewController *)webViewController
{
    
}

@end
